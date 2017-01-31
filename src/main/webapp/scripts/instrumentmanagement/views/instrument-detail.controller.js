/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController',
    function(entity, SurveySearchService, InstrumentAttachmentResource,
      StudySearchService, QuestionSearchService, PageTitleService,
      LanguageService) {
      //Controller Init
      var ctrl = this;
      ctrl.instrument = entity;
      ctrl.survey = null;
      ctrl.attachments = null;
      ctrl.study = null;
      ctrl.questionCount = null;
      //Wait for instrument Promise
      ctrl.instrument.$promise.then(function() {
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('instrument-management.' +
          'detail.page-title', {
            description: ctrl.instrument.description[currenLanguage] ?
              ctrl
              .instrument.description[currenLanguage] : ctrl
              .instrument.description[secondLanguage],
            instrumentId: ctrl.instrument.id
          });
        //load all related objects in parallel
        InstrumentAttachmentResource.findByInstrumentId({
          id: ctrl.instrument.id
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              ctrl.attachments = attachments;
            }
          });

        // Find Surveys
        SurveySearchService.findSurveys(ctrl.instrument.surveyIds).then(
          function(searchResults) {
            var foundSurveys = _.filter(searchResults.docs, function(
              searchResult) {
              return searchResult.found;
            });
            ctrl.surveyCount = foundSurveys.length;
            if (foundSurveys.length === 1) {
              ctrl.survey = foundSurveys[0]._source;
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
    });
