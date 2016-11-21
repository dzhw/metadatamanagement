'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController',
    function(entity, SurveySearchService, InstrumentAttachmentResource,
      StudySearchService, QuestionSearchService,
      QuestionSearchDialogService) {
      //Controller Init
      var ctrl = this;
      ctrl.instrument = entity;
      ctrl.survey = null;
      ctrl.attachments = null;
      ctrl.study = null;
      ctrl.questionCount = null;
      //Wait for instrument Promise
      ctrl.instrument.$promise.then(function() {
        //load all related objects in parallel
        InstrumentAttachmentResource.findByInstrumentId({
          id: ctrl.instrument.id
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              ctrl.attachments = attachments;
            }
          });
        //Find Surveys
        SurveySearchService.findSurveys([ctrl.instrument.surveyId]).then(
          function(searchResult) {
            if (searchResult.docs[0].found) {
              ctrl.survey = searchResult.docs[0]._source;
            }
          });
        //Find Studies
        StudySearchService.findStudies(
          [ctrl.instrument.dataAcquisitionProjectId]).then(
          function(searchResult) {
            if (searchResult.docs[0].found) {
              ctrl.study = searchResult.docs[0]._source;
            }
          });
        //Count By Instrument Id
        QuestionSearchService.countBy('instrumentId', ctrl.instrument.id)
          .then(function(result) {
            ctrl.questionCount = result.count;
          });
      });

      ctrl.showRelatedQuestions = function() {
        QuestionSearchDialogService.findQuestions('findByInstrumentId',
          ctrl.instrument.id, ctrl.questionCount);
      };
    });
