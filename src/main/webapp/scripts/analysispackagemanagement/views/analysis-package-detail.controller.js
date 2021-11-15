'use strict';

angular.module('metadatamanagementApp')
  .controller('AnalysisPackageDetailController',
    function(entity,
             MessageBus,
             PageMetadataService,
             LanguageService,
             $state, $location, $mdDialog,
             BreadcrumbService, Principal, SimpleMessageToastService,
             // SearchResultNavigatorService,
             // $stateParams,
             AnalysisPackageAttachmentResource,
             $rootScope, DataAcquisitionProjectResource,
             ProjectUpdateAccessService, $scope,
             $timeout, $document, AnalysisPackageOverviewResource,
             OutdatedVersionNotifier, AnalysisPackageSearchService, $log,
             blockUI, $mdSidenav, ContainsOnlyQualitativeDataChecker) {
      blockUI.start();
      // SearchResultNavigatorService
      //   .setSearchIndex($stateParams['search-result-index']);
      //
      // SearchResultNavigatorService.registerCurrentSearchResult();

      var getTags = function(analysisPackage) {
        if (analysisPackage.tags) {
          var language = LanguageService.getCurrentInstantly();
          return analysisPackage.tags[language];
        } else {
          return [];
        }
      };
      var ctrl = this;
      var activeProject;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.projectIsCurrentlyReleased = true;
      // ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        publicationsCount: 0
      };
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);
      var bowser = $rootScope.bowser;

      ctrl.loadAttachments = function() {
        AnalysisPackageAttachmentResource.findByAnalysisPackageId({
          analysisPackageId: ctrl.analysisPackage.id
        }).$promise.then(
          function(attachments) {
            ctrl.attachments = attachments;
          });
      };

      ctrl.isBetaRelease = function(analysisPackage) {
        if (analysisPackage.release) {
          return bowser.compareVersions(['1.0.0', analysisPackage
            .release.version]) === 1;
        }
        return false;
      };

      $scope.$on('deletion-completed', function() {
        //wait for 2 seconds until refresh
        //in order to wait for elasticsearch reindex
        $timeout($state.reload, 2000);
      });

      entity.promise.then(function(result) {
        var fetchFn = AnalysisPackageSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*',
            'relatedPublications']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

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
        ctrl.onlyQualitativeData = ContainsOnlyQualitativeDataChecker
          .check(result);
        if (!ctrl.onlyQualitativeData) {
          ctrl.counts.variablesCount = 0;
        }
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onAnalysisPackageChange',
            {
              masterId: result.masterId,
              version: result.release.version
            });
        }

        PageMetadataService.setPageTitle(
          'analysis-package-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()]
        });
        PageMetadataService.setPageDescription(
          'analysis-package-management.detail.page-description', {
          description: result.description[LanguageService.getCurrentInstantly()]
        });
        PageMetadataService.setDublinCoreMetadata(result);
        // sponsors create an error
        // PageMetadataService.setSchemaOrgMetadata(result);
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'analysisPackageIsPresent': true,
          'projectId': result.dataAcquisitionProjectId
        });
        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.analysisPackage = result;
          ctrl.loadAttachments();

          $timeout(function() {
            if ($location.search().query ||
              $location.search()['repeated-measurement-identifier'] ||
              $location.search()['derived-variables-identifier']) {
              ctrl.scroll();
            }
          }, 1000);
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'analysis-package-management.detail.not-released-toast',
            {id: result.id}
          );
        }

        ctrl.analysisPackageTags = getTags(result);

      }, $log.error)
        .finally(blockUI.stop);

      ctrl.scroll = function() {
        var element = $document[0].getElementById('related-objects');
        if ($rootScope.bowser.msie) {
          element.scrollIntoView(true);
        } else {
          element.scrollIntoView({behavior: 'smooth', inline: 'nearest'});
        }
      };
      ctrl.analysisPackageEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'analysisPackages', true)) {
          $state.go('analysisPackageEdit', {id: ctrl.analysisPackage.id});
        }
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };

      ctrl.generateAnalysisPackageOverview = function(event) {
        $mdDialog.show({
          controller: 'CreateOverviewDialogController',
          controllerAs: 'ctrl',
          templateUrl: 'scripts/analysispackagemanagement/' +
            'views/create-overview-dialog.html.tmpl',
          clickOutsideToClose: false,
          fullscreen: true,
          targetEvent: event
        }).then(function(result) {
          AnalysisPackageOverviewResource.startGeneration({
            analysisPackageId: ctrl.analysisPackage.id,
            version: result.version,
            languages: result.languages}).$promise.then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'data-package-management.detail.' +
                  'overview-generation-started-toast');
            });
        });
      };
    });
