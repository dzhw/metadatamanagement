'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, LanguageService,
      PageTitleService, $state, ToolbarHeaderService, SurveySearchService,
      SurveyAttachmentResource, Principal, SimpleMessageToastService) {
      var ctrl = this;
      ctrl.imgResolved = false;
      ctrl.counts = {};
      entity.promise.then(function(result) {
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('survey-management.detail.title', {
          title: result.title[currenLanguage] ? result.title[currenLanguage]
          : result.title[secondLanguage],
          surveyId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'number': result.number,
          'studyId': result.studyId,
          'projectId': result.dataAcquisitionProjectId});
        if (result.release || Principal.hasAuthority('ROLE_PUBLISHER')) {
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
            id: ctrl.survey.id
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
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
            'survey-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
      ctrl.setImgResolved = function() {
        ctrl.imgResolved = true;
      };
    });
