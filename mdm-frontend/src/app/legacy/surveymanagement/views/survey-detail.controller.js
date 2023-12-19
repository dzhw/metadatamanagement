/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController', [
  'entity',
  'LanguageService',
  'CleanJSObjectService',
  'PageMetadataService',
  '$state',
  'BreadcrumbService',
  'MessageBus',
  'SurveySearchService',
  'SurveyAttachmentResource',
  'Principal',
  'SimpleMessageToastService',
  'SearchResultNavigatorService',
  'SurveyResponseRateImageUploadService',
  'OutdatedVersionNotifier',
  'DataAcquisitionProjectResource',
  '$mdDialog',
  'ProjectUpdateAccessService',
  'CountryCodesResource',
  '$stateParams',
  'blockUI',
  '$mdSidenav',
    function(entity, LanguageService, CleanJSObjectService,
             PageMetadataService, $state, BreadcrumbService, MessageBus,
             SurveySearchService, SurveyAttachmentResource, Principal,
             SimpleMessageToastService, SearchResultNavigatorService,
             SurveyResponseRateImageUploadService, OutdatedVersionNotifier,
             DataAcquisitionProjectResource, $mdDialog,
             ProjectUpdateAccessService, CountryCodesResource, $stateParams,
             blockUI, $mdSidenav) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);
      SearchResultNavigatorService.registerCurrentSearchResult();

      var countries = CountryCodesResource.query();
      var activeProject;
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        instrumentsCount: 0,
        questionsCount: 0,
        dataSetsCount: 0,
        conceptsCount: 0
      };
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.responseRateImage = null;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      entity.promise.then(function(survey) {
        var fetchFn = SurveySearchService.findShadowByIdAndVersion
          .bind(null, survey.masterId, null, ['nested*','variables','questions',
            'instruments', 'dataSets', 'relatedPublications','concepts']);
        OutdatedVersionNotifier.checkVersionAndNotify(survey, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: survey.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null && !project.release.isPreRelease);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
          });
        }
        if (survey.dataType.en !== 'Qualitative Data') {
          ctrl.counts.variablesCount = 0;
        }
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageMetadataService.setPageTitle('survey-management.detail.title', {
          title: survey.title[currenLanguage] ? survey.title[currenLanguage]
            : survey.title[secondLanguage]
        });
        PageMetadataService.setPageDescription(
          'survey-management.detail.description', {
            population: survey.population.description[currenLanguage]
          });
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: survey.dataPackage.masterId
            });
        }
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': survey.id,
          'number': survey.number,
          'dataPackageId': survey.dataPackageId,
          'dataPackageIsPresent': CleanJSObjectService
            .isNullOrEmpty(survey.dataPackage) ? false : true,
          'projectId': survey.dataAcquisitionProjectId,
          'version': survey.shadow ? _.get(survey, 'release.version') : null
        });
        if (survey.release || Principal.hasAnyAuthority(['ROLE_PUBLISHER',
          'ROLE_DATA_PROVIDER'])) {
          ctrl.survey = survey;
          ctrl.dataPackage = survey.dataPackage;

          SurveyAttachmentResource.findBySurveyId({
            surveyId: ctrl.survey.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
          SurveyResponseRateImageUploadService.getImage(
            ctrl.survey.id, ctrl.survey.number, currenLanguage)
            .then(function(image) {
              ctrl.responseRateImage = image;
            });
          ctrl.isSimpleGeographicCoverage = false;
          if (survey.population.geographicCoverages &&
            survey.population.geographicCoverages.length === 1) {
            var descriptionDe = _.get(
              survey.population.geographicCoverages[0],
              'description.de');
            var descriptionEn = _.get(
              survey.population.geographicCoverages[0],
              'description.en');
            ctrl.isSimpleGeographicCoverage = !descriptionDe && !descriptionEn;
          }
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'survey-management.detail.not-released-toast', {id: survey.id}
          );
        }
      }).finally(blockUI.stop);

      ctrl.surveyEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'surveys', true)) {
          $state.go('surveyEdit', {id: ctrl.survey.id});
        }
      };

      ctrl.getCountryName = function(geographicCoverage) {
        var country = _.filter(countries, function(country) {
          return country.code === geographicCoverage.country;
        });

        if (country.length === 1) {
          var language = LanguageService.getCurrentInstantly();
          return country[0][language];
        } else {
          return '';
        }
      };

      ctrl.showSerialNumberInfo = function($event) {
        $mdDialog.show({
          controller: 'SerialNumberInfoController',
          templateUrl: 'scripts/surveymanagement/views/' +
              'serial-number-info.html.tmpl',
          clickOutsideToClose: true,
          escapeToClose: true,
          fullscreen: true,
          targetEvent: $event
        });
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };
    }]);

