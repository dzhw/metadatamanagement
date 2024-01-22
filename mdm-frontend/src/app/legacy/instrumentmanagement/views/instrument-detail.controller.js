/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController', [
  'entity',
  'InstrumentAttachmentResource',
  'MessageBus',
  'PageMetadataService',
  'LanguageService',
  '$state',
  'CleanJSObjectService',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'SearchResultNavigatorService',
  'DataAcquisitionProjectResource',
  'ProjectUpdateAccessService',
  'InstrumentSearchService',
  'OutdatedVersionNotifier',
  '$stateParams',
  'blockUI',
  '$mdSidenav',
  'ContainsOnlyQualitativeDataChecker',
    function(entity, InstrumentAttachmentResource, MessageBus,
             PageMetadataService, LanguageService, $state, CleanJSObjectService,
             BreadcrumbService, Principal, SimpleMessageToastService,
             SearchResultNavigatorService,
             DataAcquisitionProjectResource, ProjectUpdateAccessService,
             InstrumentSearchService, OutdatedVersionNotifier, $stateParams,
             blockUI, $mdSidenav, ContainsOnlyQualitativeDataChecker) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var activeProject;
      //Controller Init
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        surveysCount: 0,
        questionsCount: 0,
        dataSetsCount: 0,
        conceptsCount: 0
      };
      ctrl.survey = null;
      ctrl.attachments = null;
      ctrl.dataPackage = null;
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      //Wait for instrument Promise
      entity.promise.then(function(result) {
        var fetchFn = InstrumentSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*','questions', 'dataSets',
            'variables','relatedPublications','concepts']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.project = project;
            ctrl.projectIsCurrentlyReleased = (project.release != null && !project.release.isPreRelease);
            ctrl.shouldDisplayEditButton = localStorage.getItem('currentView') != 'orderView' && !(project.release != null && !project.release.isPreRelease);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
            ctrl.isProviderView = localStorage.getItem('currentView') != 'orderView';
          });
        }
        ctrl.onlyQualitativeData = ContainsOnlyQualitativeDataChecker
          .check(result);
        if (!ctrl.onlyQualitativeData) {
          ctrl.counts.variablesCount = 0;
        }
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'instrumentIsPresent': true,
          'surveys': result.surveys,
          'dataPackageId': result.dataPackageId,
          'dataPackageIsPresent': CleanJSObjectService
            .isNullOrEmpty(result.dataPackage) ? false : true,
          'projectId': result.dataAcquisitionProjectId,
          'version': result.shadow ? _.get(result, 'release.version') : null
        });
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: result.dataPackage.masterId
            });
        }
        PageMetadataService.setPageTitle('instrument-management.' +
          'detail.page-title', {
          description: result.description[currenLanguage] ?
            result.description[currenLanguage] :
            result.description[secondLanguage]
        });

        PageMetadataService.setPageDescription('instrument-management.' +
          'detail.page-description', {
            title: result.title[currenLanguage] ?
              result.title[currenLanguage] :
              result.title[secondLanguage]
          });

        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.instrument = result;
          //load all related objects in parallel
          InstrumentAttachmentResource.findByInstrumentId({
            instrumentId: ctrl.instrument.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'instrument-management.detail.not-released-toast', {id: result.id}
          );
        }
      }).finally(blockUI.stop);

      ctrl.instrumentEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'instruments', true)) {
          $state.go('instrumentEdit', {id: ctrl.instrument.id});
        }
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };
    }]);

