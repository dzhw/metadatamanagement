'use strict';

/**
 * View implementing the detail page of a data package. The detail page
 * displays basic information about the data package and links to its components.
 * The view is accessible for all users but provides options to switch to
 * editing mode for PUBLISHERS and DATAPROVIDERS. It also offers the 
 * opportunity to generate the overview of the data package as a PDF file.
 */
angular.module('metadatamanagementApp')
  .controller('DataPackageDetailController', [
  'entity',
  'MessageBus',
  'PageMetadataService',
  'LanguageService',
  '$state',
  '$location',
  '$mdDialog',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'SearchResultNavigatorService',
  '$stateParams',
  'DataAcquisitionProjectAttachmentsResource',
  '$rootScope',
  'DataAcquisitionProjectResource',
  'ProjectUpdateAccessService',
  '$scope',
  '$timeout',
  '$document',
  'DataPackageOverviewResource',
  'OutdatedVersionNotifier',
  'DataPackageSearchService',
  '$log',
  'blockUI',
  '$mdSidenav',
  'ContainsOnlyQualitativeDataChecker',
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
      var getTagsElsst = function(dataPackage) {
        if (dataPackage.tagsElsst) {
          var language = LanguageService.getCurrentInstantly();
          return dataPackage.tagsElsst[language];
        } else {
          return [];
        }
      };
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.projectIsCurrentlyReleased = true;
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
      ctrl.showRemarks = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER']);

      var bowser = $rootScope.bowser;

      /**
       * Method for loading attachments
       */
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

      /**
       * Whether the data package is beta released (version < 1.0.0) or not.
       * @param {*} dataPackage 
       * @returns true if it is a beta release else false
       */
      ctrl.isBetaRelease = function(dataPackage) {
        if (dataPackage.release) {
          return bowser.compareVersions(['1.0.0', dataPackage
            .release.version]) === 1;
        }
        return false;
      };

      /**
       * Listener for deletion event
       */
      $scope.$on('deletion-completed', function() {
        //wait for 2 seconds until refresh
        //in order to wait for elasticsearch reindex
        $timeout($state.reload, 2000);
      });

      /**
       * init
       */
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
            ctrl.projectIsCurrentlyReleased = (project.release != null && !project.release.isPreRelease);
            ctrl.shouldDisplayEditButton = localStorage.getItem(
              'currentView') != 'orderView' && !(project.release != null && !project.release.isPreRelease);
            ctrl.isProviderView = localStorage.getItem('currentView') != 'orderView';
            ctrl.project = project;
            ctrl.isProviderView = localStorage.getItem('currentView') != 'orderView';
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
        ctrl.dataPackageTagsElsst = getTagsElsst(result);

      }, $log.error).finally(blockUI.stop);

      /**
       * Scrolling handler
       */
      ctrl.scroll = function() {
        var element = $document[0].getElementById('related-objects');
        if ($rootScope.bowser.msie) {
          element.scrollIntoView(true);
        } else {
          element.scrollIntoView({behavior: 'smooth', inline: 'nearest'});
        }
      };

      /**
       * Method to check if edits are allowed and switch to editing page of the
       * current data package if so.
       */
      ctrl.dataPackageEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(ctrl.project, 'dataPackages', true)) {
          $state.go('dataPackageEdit', {id: ctrl.dataPackage.id});
        }
      };

      /**
       * Method to toggle the side nav menu.
       */
      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };

      /**
       * Method to generate the data package overview as a PDF.
       * @param {*} event 
       */
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

      ctrl.getTranslationPathFromApprovedUsageId = function(id) {
        switch(id) {
          case 'SCIENTIFIC_USE':
            return "data-package-management.common.approvedUsage.scientificUse"
          case 'TEACHING_PURPOSES':
            return "data-package-management.common.approvedUsage.teachingPurposes"
          case 'NON_COMMERCIAL_USE':
            return "data-package-management.common.approvedUsage.nonCommercialUse"
          case 'COMMERCIAL_USE':
            return "data-package-management.common.approvedUsage.commercialUse"
          default:
            console.log("ApprovedUsageId (" + id + ") unknown!");
            return "unknown"
        }
      }

      ctrl.isPublisher = function() {
        return Principal.isPublisher();
      };

      /**
       * Whether a warning about the embargo date of the project should be
       * displayed.
       * @returns true if embargo date is applied else false
       */
      ctrl.shouldDisplayEmbargoWarning = function() {
        return ctrl.dataPackage.release.isPreRelease;
      }

      /**
       * Whether the embargo date has expired or not.
       * @returns true if it has expired else false
       */
      ctrl.isEmbargoDateExpired = function() {
        if (ctrl.dataPackage.embargoDate) {
          var current = new Date();
          return new Date(ctrl.dataPackage.embargoDate) < current;
        }
        return true;
      }

      /**
       * Displays an info modal.
       * @param {*} $event the click event
       */
      ctrl.infoModal = function( $event) {
        $mdDialog.show({
          controller: 'dataPackageInfoController',
          templateUrl: 'scripts/datapackagemanagement/components/elsst-info.html.tmpl',
          clickOutsideToClose: true,
          escapeToClose: true,
          fullscreen: true,
          targetEvent: $event
        });
      };
    }]);

