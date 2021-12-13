/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('AnalysisPackageDetailController',
    function(entity,
             MessageBus,
             PageMetadataService,
             LanguageService, DataPackageSearchService,
             $state, $location,
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
      var ctrl = this;
      var activeProject;
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
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        publicationsCount: 0
      };
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      ctrl.loadAttachments = function() {
        AnalysisPackageAttachmentResource.findByAnalysisPackageId({
          analysisPackageId: ctrl.analysisPackage.id
        }).$promise.then(
          function(attachments) {
            ctrl.attachments = attachments;
          });
      };

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

      ctrl.isBetaRelease = function(analysisPackage) {
        if (analysisPackage.release) {
          return bowser.compareVersions(['1.0.0', analysisPackage
            .release.version]) === 1;
        }
        return false;
      };

      ctrl.loadScriptAttachments = function() {
        ScriptAttachmentResource.findByAnalysisPackageId({
          analysisPackageId: ctrl.analysisPackage.id
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              ctrl.scriptAttachments = attachments;
              getScriptAttachmentMetadata();
            }
          });
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

        if (!Principal.isAuthenticated()) {
          MessageBus.set('onAnalysisPackageChange',
            {
              masterId: result.masterId,
              version: result.release.version
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
        PageMetadataService.setDublinCoreMetadata(result, true);
        PageMetadataService.setSchemaOrgMetadata(result, true);
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
    });
