/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, LanguageService, CleanJSObjectService,
      PageTitleService, $state, ToolbarHeaderService, SurveySearchService,
      SurveyAttachmentResource, Principal, SimpleMessageToastService,
      SearchResultNavigatorService, $stateParams,
      SurveyResponseRateImageUploadService, DataAcquisitionProjectResource,
      ProductChooserDialogService, ProjectUpdateAccessService) {
      SearchResultNavigatorService.registerCurrentSearchResult(
          $stateParams['search-result-index']);
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = $stateParams['search-result-index'];
      ctrl.counts = {};
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER','ROLE_ADMIN']);

      ctrl.jsonExcludes = [
        'nestedStudy',
        'nestedDataSets',
        'nestedVariables',
        'nestedRelatedPublications',
        'nestedInstruments',
        'nestedQuestions'
      ];

      entity.promise.then(function(result) {
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null);
            ctrl.isUpdateAllowed = ProjectUpdateAccessService.isUpdateAllowed();
          });
        }
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('survey-management.detail.title', {
          title: result.title[currenLanguage] ? result.title[currenLanguage]
          : result.title[secondLanguage],
          surveyId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'studyId': result.studyId,
          'studyIsPresent': CleanJSObjectService.
          isNullOrEmpty(result.study) ? false : true,
          'projectId': result.dataAcquisitionProjectId});
        if (result.dataSets) {
          ctrl.accessWays = [];
          result.dataSets.forEach(function(dataSet) {
            ctrl.accessWays = _.union(dataSet.accessWays, ctrl.accessWays);
          });
        }
        if (result.release || Principal.hasAnyAuthority(['ROLE_PUBLISHER',
            'ROLE_DATA_PROVIDER'])) {
          ctrl.survey = result;
          ctrl.study = result.study;
          ctrl.counts.dataSetsCount = result.dataSets.length;
          if (ctrl.counts.dataSetsCount === 1) {
            ctrl.dataSet = result.dataSets[0];
          }
          SurveySearchService.countBy('dataAcquisitionProjectId',
          ctrl.survey.dataAcquisitionProjectId)
          .then(function(surveysCount) {
            ctrl.counts.surveysCount = surveysCount.count;
          });
          ctrl.counts.instrumentsCount = result.instruments.length;
          if (ctrl.counts.instrumentsCount === 1) {
            ctrl.instrument = result.instruments[0];
          }
          SurveyAttachmentResource.findBySurveyId({
            surveyId: ctrl.survey.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
          ctrl.counts.publicationsCount = result.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
          SurveyResponseRateImageUploadService.getImage(
            ctrl.survey.id, ctrl.survey.number, currenLanguage)
            .then(function(image) {
              ctrl.responseRateImage = image;
            });
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'survey-management.detail.not-released-toast', {id: result.id}
          );
        }
      });

      ctrl.addToShoppingCart = function(event) {
        ProductChooserDialogService.showDialog(
          ctrl.survey.dataAcquisitionProjectId, ctrl.accessWays,
          ctrl.survey.study,
          event);
      };
    });
