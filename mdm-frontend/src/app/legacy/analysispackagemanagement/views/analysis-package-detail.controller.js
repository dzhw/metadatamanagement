/* globals _ */
'use strict';

/**
 * View implementing the detail page of an analysis package. The detail page
 * displays basic information about the analysis package and links to its components.
 * The view is accessible for all users but provides options to switch to
 * editing mode for PUBLISHERS and DATAPROVIDERS. It also offers the
 * opportunity to generate the overview of the analysis package as a PDF file.
 */
angular.module('metadatamanagementApp')
  .controller('AnalysisPackageDetailController', [
  'entity',
  'MessageBus',
  'PageMetadataService',
  'LanguageService',
  'DataPackageSearchService',
  '$state',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'SearchResultNavigatorService',
  '$stateParams',
  'AnalysisPackageAttachmentResource',
  '$rootScope',
  'DataAcquisitionProjectResource',
  'ProjectUpdateAccessService',
  '$scope',
  'ScriptAttachmentResource',
  '$timeout',
  '$document',
  'OutdatedVersionNotifier',
  'AnalysisPackageSearchService',
  '$log',
  'blockUI',
  '$mdSidenav',
  'ContainsOnlyQualitativeDataChecker',
    function(entity,
             MessageBus,
             PageMetadataService,
             LanguageService, DataPackageSearchService,
             $state,
             BreadcrumbService, Principal, SimpleMessageToastService,
             SearchResultNavigatorService,
             $stateParams,
             AnalysisPackageAttachmentResource,
             $rootScope, DataAcquisitionProjectResource,
             ProjectUpdateAccessService, $scope, ScriptAttachmentResource,
             $timeout, $document,
             OutdatedVersionNotifier, AnalysisPackageSearchService, $log,
             blockUI, $mdSidenav, ContainsOnlyQualitativeDataChecker) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);
      SearchResultNavigatorService.registerCurrentSearchResult();

      var getScriptAttachmentMetadata = function() {
        _.forEach(ctrl.analysisPackage.scripts, function(value, index) {
          _.filter(ctrl.scriptAttachments, function(item) {
            if (item.scriptUuid === value.uuid) {
              ctrl.analysisPackage.scripts[index].attachment = item;
            }
          });
        });
      };

      var getTags = function(analysisPackage) {
        if (analysisPackage.tags) {
          var language = LanguageService.getCurrentInstantly();
          return analysisPackage.tags[language];
        } else {
          return [];
        }
      };
      var getTagsElsst = function(analysisPackage) {
        if (analysisPackage.tagsElsst) {
          var language = LanguageService.getCurrentInstantly();
          return analysisPackage.tagsElsst[language];
        } else {
          return [];
        }
      };
      var ctrl = this;
      var bowser = $rootScope.bowser;

      ctrl.dataPackageList = {
        dataPackage: {
          en: 'FDZ-DZHW Datapackage',
          de: 'FDZ-DZHW Datenpaket'
        },
        externalData: {
          en: 'External Data Package',
          de: 'Externes Datenpaket'
        },
        customDataPackage: {
          en: 'Custom Data Package',
          de: 'Benutzerdefiniertes Datenpaket'
        }
      };
      ctrl.dataPackage = null;
      ctrl.scriptAttachments = [];
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        publicationsCount: 0
      };
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      /**
       * Method for loading attachments
       */
      ctrl.loadAttachments = function() {
        AnalysisPackageAttachmentResource.findByAnalysisPackageId({
          analysisPackageId: ctrl.analysisPackage.id
        }).$promise.then(
          function(attachments) {
            ctrl.attachments = attachments;
          });
      };

      /**
       * Method for loading data packages
       */
      ctrl.loadDataPackages = function(packages) {
        var excludes = ['nested*', 'variables', 'questions',
          'surveys', 'instruments', 'relatedPublications',
          'concepts'];

        _.forEach(packages, function(item, index) {
          if (item.type === 'dataPackage') {
            DataPackageSearchService
              .findOneById(item.dataPackageMasterId, ['title'], excludes)
              .promise
              .then(function(data) {
                packages[index].dataPackageTitle = data.title;
              });
          }
        });
      };

      /**
       * Whether the analysis package is beta released (version < 1.0.0) or not.
       * @param {*} analysisPackage
       * @returns true if it is a beta release else false
       */
      ctrl.isBetaRelease = function(analysisPackage) {
        if (analysisPackage.release) {
          return bowser.compareVersions(['1.0.0', analysisPackage
            .release.version]) === 1;
        }
        return false;
      };

      /**
       * Method for loading script attachments
       */
      ctrl.loadScriptAttachments = function() {
        ScriptAttachmentResource.findByAnalysisPackageId({
          analysisPackageId: ctrl.analysisPackage.id
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              ctrl.scriptAttachments = attachments;
              getScriptAttachmentMetadata();
              ctrl.analysisPackage.scripts.forEach(function(script) {
                if (!script.attachment) {
                  ctrl.analysisPackage.scripts
                    .containsScriptWithoutAttachment = true;
                }
              });
            } else {
              ctrl.analysisPackage.scripts.containsScriptWithoutAttachment =
                true;
            }
          });
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
        var fetchFn = AnalysisPackageSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*',
            'relatedPublications']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.shouldDisplayEditButton = localStorage.getItem('currentView') != 'orderView' && !(project.release != null && !project.release.isPreRelease);
            ctrl.isProviderView = localStorage.getItem('currentView') != 'orderView';
            ctrl.project = project;
          });
        }
        ctrl.onlyQualitativeData = ContainsOnlyQualitativeDataChecker
          .check(result);
        // trigger MessageBus for showing ordering options in the sidenav
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onAnalysisPackageChange',
            {
              masterId: result.masterId
            });
          MessageBus.set('onDetailViewLoaded', {type: 'analysisPackage'});
        } else {
          MessageBus.set('onAnalysisPackageChange',
            {
              masterId: result.masterId,
              projectId: result.dataAcquisitionProjectId
            });
          MessageBus.set('onDetailViewLoaded', {type: 'analysisPackage'});
        }

        PageMetadataService.setPageTitle(
          'analysis-package-management.detail.title', {
            title: result.title[LanguageService.getCurrentInstantly()]
          });
        PageMetadataService.setPageDescription(
          'analysis-package-management.detail.page-description', {
            description: result.description[
              LanguageService.getCurrentInstantly()
              ]
          });
        PageMetadataService.setDublinCoreMetadataForAP(result);
        PageMetadataService.setSchemaOrgMetadataForAP(result);
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'analysisPackageIsPresent': true,
          'projectId': result.dataAcquisitionProjectId
        });
        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.analysisPackage = result;
          ctrl.loadDataPackages(result.analysisDataPackages);
          ctrl.loadAttachments();
          ctrl.loadScriptAttachments();
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'analysis-package-management.detail.not-released-toast',
            {id: result.id}
          );
        }

        ctrl.analysisPackageTags = getTags(result);
        ctrl.analysisPackageTagsElsst = getTagsElsst(result);

      }, $log.error)
        .finally(blockUI.stop);

        /**
         * Scrolling handler.
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
       * current analysis package if so.
       */
      ctrl.analysisPackageEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(ctrl.project, 'analysisPackages', true)) {
          $state.go('analysisPackageEdit', {id: ctrl.analysisPackage.id});
        }
      };

      /**
       * Method to toggle the side nav menu.
       */
      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };

       /**
       * Whether a warning about the embargo date of the project should be
       * displayed.
       * @returns true if embargo date is applied else false
       */
       ctrl.shouldDisplayEmbargoWarning = function() {
        return ctrl.analysisPackage.release.isPreRelease;
      }

      /**
       * Whether the embargo date has expired or not.
       * @returns true if it has expired else false
       */
      ctrl.isEmbargoDateExpired = function() {
        if (ctrl.analysisPackage.embargoDate) {
          var current = new Date();
          return new Date(ctrl.analysisPackage.embargoDate) < current;
        }
        return true;
      };
    }]);

