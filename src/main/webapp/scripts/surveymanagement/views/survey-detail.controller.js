/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, LanguageService, CleanJSObjectService,
             PageTitleService, $state, ToolbarHeaderService, MessageBus,
             SurveySearchService, SurveyAttachmentResource, Principal,
             SimpleMessageToastService, SearchResultNavigatorService,
             SurveyResponseRateImageUploadService, OutdatedVersionNotifier,
             DataAcquisitionProjectResource, ProductChooserDialogService,
             ProjectUpdateAccessService, CountryCodesResource, $stateParams,
             blockUI) {
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
      ctrl.counts = {};
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      ctrl.jsonExcludes = [
        'nestedStudy',
        'nestedDataSets',
        'nestedVariables',
        'nestedRelatedPublications',
        'nestedInstruments',
        'nestedQuestions',
        'nestedConcepts'
      ];

      entity.promise.then(function(survey) {
        var fetchFn = SurveySearchService.findShadowByIdAndVersion
          .bind(null, survey.masterId);
        OutdatedVersionNotifier.checkVersionAndNotify(survey, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: survey.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
          });
        }
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('survey-management.detail.title', {
          title: survey.title[currenLanguage] ? survey.title[currenLanguage]
            : survey.title[secondLanguage],
          surveyId: survey.id
        });
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: survey.study.masterId,
              version: survey.release.version
            });
        }
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': survey.id,
          'number': survey.number,
          'studyId': survey.studyId,
          'studyIsPresent': CleanJSObjectService.isNullOrEmpty(survey.study) ?
            false : true,
          'projectId': survey.dataAcquisitionProjectId,
          'version': survey.shadow ? _.get(survey, 'release.version') : null
        });
        if (survey.dataSets) {
          ctrl.accessWays = [];
          survey.dataSets.forEach(function(dataSet) {
            ctrl.accessWays = _.union(dataSet.accessWays, ctrl.accessWays);
          });
        }
        if (survey.release || Principal.hasAnyAuthority(['ROLE_PUBLISHER',
          'ROLE_DATA_PROVIDER'])) {
          ctrl.survey = survey;
          ctrl.study = survey.study;
          ctrl.counts.dataSetsCount = survey.dataSets.length;
          if (ctrl.counts.dataSetsCount === 1) {
            ctrl.dataSet = survey.dataSets[0];
          }

          if (!Principal.isAuthenticated()) {
            SurveySearchService.countBy('dataAcquisitionProjectId',
              ctrl.survey.dataAcquisitionProjectId,
              _.get(survey, 'release.version'))
              .then(function(surveysCount) {
                ctrl.counts.surveysCount = surveysCount.count;
              });
          }
          ctrl.counts.instrumentsCount = survey.instruments.length;
          if (ctrl.counts.instrumentsCount === 1) {
            ctrl.instrument = survey.instruments[0];
          }
          SurveyAttachmentResource.findBySurveyId({
            surveyId: ctrl.survey.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
          ctrl.counts.publicationsCount = survey.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = survey.relatedPublications[0];
          }
          ctrl.counts.questionsCount = survey.questions.length;
          if (ctrl.counts.questionsCount === 1) {
            ctrl.question = survey.questions[0];
          }
          ctrl.counts.conceptsCount = survey.concepts.length;
          if (ctrl.counts.conceptsCount === 1) {
            ctrl.concept = survey.concepts[0];
          }
          SurveyResponseRateImageUploadService.getImage(
            ctrl.survey.id, ctrl.survey.number, currenLanguage)
            .then(function(image) {
              ctrl.responseRateImage = image;
            });
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'survey-management.detail.not-released-toast', {id: survey.id}
          );
        }
      }).finally(blockUI.stop);

      ctrl.addToShoppingCart = function(event) {
        ProductChooserDialogService.showDialog(
          ctrl.survey.dataAcquisitionProjectId, ctrl.accessWays,
          ctrl.survey.study, ctrl.survey.release.version,
          event);
      };

      ctrl.surveyEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'surveys', true)) {
          $state.go('surveyEdit', {id: ctrl.survey.id});
        }
      };

      ctrl.isSimpleGeographicCoverage = function(geographicCoverages) {
        if (geographicCoverages && geographicCoverages.length === 1) {
          var descriptionDe = _.get(geographicCoverages[0], 'description.de');
          var descriptionEn = _.get(geographicCoverages[0], 'description.en');

          return !descriptionDe && !descriptionEn;
        } else {
          return false;
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
    });
