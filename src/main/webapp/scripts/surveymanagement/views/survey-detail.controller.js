/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, LanguageService, CleanJSObjectService,
             PageTitleService, $state, ToolbarHeaderService,
             SurveySearchService, SurveyAttachmentResource, Principal,
             SimpleMessageToastService, SearchResultNavigatorService,
             $stateParams, SurveyResponseRateImageUploadService,
             DataAcquisitionProjectResource, ProductChooserDialogService,
             ProjectUpdateAccessService) {

      SearchResultNavigatorService.registerCurrentSearchResult(
        $stateParams['search-result-index']);
      var activeProject;
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = $stateParams['search-result-index'];
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
        'nestedQuestions'
      ];

      entity.promise.then(function(survey) {
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
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': survey.id,
          'number': survey.number,
          'studyId': survey.studyId,
          'studyIsPresent': CleanJSObjectService.isNullOrEmpty(survey.study) ? false : true,
          'projectId': survey.dataAcquisitionProjectId
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
          SurveySearchService.countBy('dataAcquisitionProjectId',
            ctrl.survey.dataAcquisitionProjectId)
            .then(function(surveysCount) {
              ctrl.counts.surveysCount = surveysCount.count;
            });
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
      });

      ctrl.addToShoppingCart = function(event) {
        ProductChooserDialogService.showDialog(
          ctrl.survey.dataAcquisitionProjectId, ctrl.accessWays,
          ctrl.survey.study,
          event);
      };

      ctrl.surveyEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'surveys', true)) {
          $state.go('surveyEdit', {id: ctrl.survey.id});
        }
      };
    });
