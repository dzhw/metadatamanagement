'use strict';

angular.module('metadatamanagementApp')
  .controller('DataPackageDetailController',
    function(entity,
             MessageBus,
             PageMetadataService,
             LanguageService,
             $state, $location, $mdDialog,
             BreadcrumbService, Principal, SimpleMessageToastService,
             SearchResultNavigatorService,
             $stateParams,
             DataAcquisitionProjectAttachmentsResource,
             $rootScope, DataAcquisitionProjectResource,
             ProjectUpdateAccessService, $scope,
             $timeout, $document, DataPackageOverviewResource,
             OutdatedVersionNotifier, DataPackageSearchService, $log,
             blockUI, $mdSidenav, ContainsOnlyQualitativeDataChecker) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();

      var getTags = function(dataPackage) {
        if (dataPackage.tags) {
          var language = LanguageService.getCurrentInstantly();
          return dataPackage.tags[language];
        } else {
          return [];
        }
      };
      var ctrl = this;
      var activeProject;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.hasBeenReleasedBefore = false;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        surveysCount: 0,
        instrumentsCount: 0,
        questionsCount: 0,
        dataSetsCount: 0,
        publicationsCount: 0,
        conceptsCount: 0
      };
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);
      var bowser = $rootScope.bowser;

      ctrl.loadAttachments = function() {
        DataAcquisitionProjectAttachmentsResource.get({
          id: ctrl.dataPackage.dataAcquisitionProjectId
        }).$promise.then(
          function(attachments) {
            if (attachments) {
              ctrl.attachments = attachments;
            }
          });
      };

      ctrl.isBetaRelease = function(dataPackage) {
        if (dataPackage.release) {
          return bowser.compareVersions(['1.0.0', dataPackage
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
        var fetchFn = DataPackageSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*','variables','questions',
            'surveys','instruments', 'dataSets', 'relatedPublications',
            'concepts']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
            ctrl.hasBeenReleasedBefore = project.hasBeenReleasedBefore;
          });
        }
        ctrl.onlyQualitativeData = ContainsOnlyQualitativeDataChecker
          .check(result);
        if (!ctrl.onlyQualitativeData) {
          ctrl.counts.variablesCount = 0;
        }
        // trigger MessageBus for showing ordering options in the sidenav
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: result.masterId
            });
          MessageBus.set('onDetailViewLoaded', {type: 'dataPackage'});
        } else {
          // set version to null (to prevent reloading a specific version)
          MessageBus.set('onDataPackageChange',
          {
            masterId: result.masterId,
            projectId: result.dataAcquisitionProjectId
          });
          MessageBus.set('onDetailViewLoaded', {type: 'dataPackage'});
        }

        PageMetadataService.setPageTitle(
          'data-package-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()]
        });
        PageMetadataService.setPageDescription(
          'data-package-management.detail.page-description', {
          description: result.description[LanguageService.getCurrentInstantly()]
        });
        PageMetadataService.setDublinCoreMetadata(result);
        PageMetadataService.setSchemaOrgMetadata(result);
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'dataPackageIsPresent': true,
          'projectId': result.dataAcquisitionProjectId
        });
        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.dataPackage = result;
          // $scope.$broadcast('currentProjectChanged', result);
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
            'data-package-management.detail.not-released-toast', {id: result.id}
          );
        }

        ctrl.dataPackageTags = getTags(result);

      }, $log.error).finally(blockUI.stop);

      ctrl.scroll = function() {
        var element = $document[0].getElementById('related-objects');
        if ($rootScope.bowser.msie) {
          element.scrollIntoView(true);
        } else {
          element.scrollIntoView({behavior: 'smooth', inline: 'nearest'});
        }
      };
      ctrl.dataPackageEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'dataPackages', true)) {
          $state.go('dataPackageEdit', {id: ctrl.dataPackage.id});
        }
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };

      ctrl.generateDataPackageOverview = function(event) {
        $mdDialog.show({
          controller: 'CreateOverviewDialogController',
          controllerAs: 'ctrl',
          templateUrl: 'scripts/datapackagemanagement/' +
            'views/create-overview-dialog.html.tmpl',
          clickOutsideToClose: false,
          fullscreen: true,
          targetEvent: event
        }).then(function(result) {
          DataPackageOverviewResource.startGeneration({
            dataPackageId: ctrl.dataPackage.id,
            version: result.version,
            languages: result.languages}).$promise.then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'data-package-management.detail.' +
                  'overview-generation-started-toast');
            });
        });
      };
    });
