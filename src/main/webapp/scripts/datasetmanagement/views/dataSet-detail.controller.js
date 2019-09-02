/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController',
    function(entity, Principal,
             VariableSearchService, ProjectUpdateAccessService,
             DataSetSearchService, DataSetReportResource, PageTitleService,
             LanguageService, $state, ToolbarHeaderService,
             CleanJSObjectService, SimpleMessageToastService,
             DataSetAttachmentResource, DataSetCitateDialogService,
             SearchResultNavigatorService, ProductChooserDialogService,
             DataAcquisitionProjectResource, OutdatedVersionNotifier,
             $stateParams, blockUI, $mdDialog) {
      blockUI.start();

      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var activeProject;
      var ctrl = this;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAnyAuthority = Principal.hasAnyAuthority;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.counts = {};
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.openDialog = function(subDataSet, event) {
        DataSetCitateDialogService.showDialog(subDataSet.citationHint, event);
      };
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_ADMIN', 'ROLE_PUBLISHER']);

      ctrl.jsonExcludes = [
        'nestedStudy',
        'nestedVariables',
        'nestedInstruments',
        'nestedQuestions',
        'nestedRelatedPublications',
        'nestedSurveys',
        'nestedConcepts'
      ];

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
          .bind(null, result.masterId);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        var currentLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currentLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('data-set-management.detail.title', {
          description: result.description[currentLanguage] ? result
            .description[currentLanguage] : result.description[secondLanguage],
          dataSetId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'studyId': result.studyId,
          'surveys': result.surveys,
          'dataSetIsPresent': true,
          'studyIsPresent': CleanJSObjectService.isNullOrEmpty(result.study) ?
            false : true,
          'projectId': result.dataAcquisitionProjectId,
          'version': result.shadow ? _.get(result, 'release.version') : null
        });
        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.dataSet = result;
          ctrl.study = result.study;
          ctrl.counts.surveysCount = result.surveys.length;
          if (ctrl.counts.surveysCount === 1) {
            ctrl.survey = result.surveys[0];
          }
          ctrl.counts.variablesCount = result.variables.length;
          if (ctrl.counts.variablesCount === 1) {
            ctrl.variable = result.variables[0];
          }
          ctrl.counts.publicationsCount = result.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
          ctrl.counts.conceptsCount = result.concepts.length;
          if (ctrl.counts.conceptsCount === 1) {
            ctrl.concept = result.concepts[0];
          }
          DataSetSearchService
            .countBy('dataAcquisitionProjectId',
              ctrl.dataSet.dataAcquisitionProjectId,
              _.get(result, 'release.version'))
            .then(function(dataSetsCount) {
              ctrl.counts.dataSetsCount = dataSetsCount.count;
            });
          ctrl.accessWays = [];
          ctrl.dataSet.subDataSets.forEach(function(subDataSet) {
            ctrl.accessWays.push(subDataSet.accessWay);
            VariableSearchService.countBy('accessWays',
              subDataSet.accessWay, ctrl.dataSet.id,
              _.get(result, 'release.version')).then(function(counts) {
              ctrl.counts[subDataSet.name] = counts.count;
            });
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
        }).then(function(version) {
          DataSetReportResource.startGeneration({
            dataSetId: ctrl.dataSet.id,
            version: version}).$promise.then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'data-set-management.detail.report-generation-started-toast');
            });
        });
      };

      ctrl.addToShoppingCart = function(event) {
        ProductChooserDialogService.showDialog(
          ctrl.dataSet.dataAcquisitionProjectId, ctrl.dataSet.accessWays,
          ctrl.dataSet.study,
          ctrl.dataSet.release.version,
          event);
      };

      ctrl.dataSetEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'data_sets', true)) {
          $state.go('dataSetEdit', {id: ctrl.dataSet.id});
        }
      };
    });
