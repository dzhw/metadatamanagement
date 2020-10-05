/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController',
    function(entity, Principal,
             VariableSearchService, ProjectUpdateAccessService,
             DataSetSearchService, DataSetReportResource, PageTitleService,
             LanguageService, $state, BreadcrumbService,
             CleanJSObjectService, SimpleMessageToastService,
             DataSetAttachmentResource,
             SearchResultNavigatorService,
             DataAcquisitionProjectResource, OutdatedVersionNotifier,
             $stateParams, blockUI, $mdDialog, MessageBus, LocationSimplifier,
             $mdSidenav) {
      blockUI.start();
      LocationSimplifier.removeDollarSign();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var activeProject;
      var ctrl = this;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAnyAuthority = Principal.hasAnyAuthority;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.counts = {
        surveysCount: 0,
        variablesCount: 0,
        conceptsCount: 0
      };
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_ADMIN', 'ROLE_PUBLISHER']);

      entity.promise.then(function(result) {
        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
          });
        }

        var fetchFn = DataSetSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*','variables','questions',
            'instruments', 'relatedPublications','concepts']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: result.dataPackage.masterId,
              version: result.release.version
            });
        }

        var currentLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currentLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('data-set-management.detail.title', {
          description: result.description[currentLanguage] ? result
            .description[currentLanguage] : result.description[secondLanguage],
          dataSetId: result.id
        });
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'dataPackageId': result.dataPackageId,
          'surveys': result.surveys,
          'dataSetIsPresent': true,
          'dataPackageIsPresent': CleanJSObjectService
            .isNullOrEmpty(result.dataPackage) ? false : true,
          'projectId': result.dataAcquisitionProjectId,
          'version': result.shadow ? _.get(result, 'release.version') : null
        });
        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.dataSet = result;
          ctrl.dataPackage = result.dataPackage;
          ctrl.dataSet.subDataSets.forEach(function(subDataSet) {
            VariableSearchService.countBy('accessWays',
              subDataSet.accessWay, ctrl.dataSet.id,
              _.get(result, 'release.version')).then(function(counts) {
              ctrl.counts[subDataSet.name] = counts.count;
            });
          });
          VariableSearchService.countBy(null,
            null, ctrl.dataSet.id,
            _.get(result, 'release.version')).then(function(counts) {
            ctrl.counts.variablesCount = counts.count;
          });
          DataSetAttachmentResource.findByDataSetId({
            dataSetId: ctrl.dataSet.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'data-set-management.detail.not-released-toast', {id: result.id}
          );
        }
      }).finally(blockUI.stop);
      ctrl.generateDataSetReport = function() {
        $mdDialog.show({
          controller: 'CreateReportDialogController',
          controllerAs: 'ctrl',
          templateUrl: 'scripts/datasetmanagement/' +
            'views/create-report-dialog.html.tmpl',
          clickOutsideToClose: false,
          fullscreen: true
        }).then(function(result) {
          DataSetReportResource.startGeneration({
            dataSetId: ctrl.dataSet.id,
            version: result.version,
            languages: result.languages}).$promise.then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'data-set-management.detail.report-generation-started-toast');
            });
        });
      };

      ctrl.dataSetEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'data_sets', true)) {
          $state.go('dataSetEdit', {id: ctrl.dataSet.id});
        }
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };
    });
