/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController',
    function(entity, SurveySearchService, InstrumentAttachmentResource,
      StudySearchService, QuestionSearchService, PageTitleService,
      LanguageService, RelatedPublicationSearchService, $state,
      ToolbarHeaderService) {
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
        SurveySearchService.findSurveys(ctrl.instrument.surveyIds,
          ['dataAcquisitionProjectId', 'number', 'title', 'id']).then(
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

        //Find Study
        StudySearchService.findOneByProjectId(ctrl.instrument.
          dataAcquisitionProjectId, ['dataAcquisitionProjectId','title', 'id'])
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        //Count By Instrument Id
        QuestionSearchService.countBy('instrumentId', ctrl.instrument.id)
          .then(function(result) {
            ctrl.questionCount = result.count;
            if (ctrl.questionCount === 1) {
              QuestionSearchService
              .findOneByInstrumentId(ctrl.instrument.id, [
                'dataAcquisitionProjectId', 'questionText',
                'instrumentNumber', 'number', 'id'])
              .then(function(question) {
                ctrl.question = question.
                hits.hits[0]._source;
              });
            }
          });

        RelatedPublicationSearchService.countBy('instrumentIds',
          ctrl.instrument.id).then(function(result) {
            ctrl.publicationCount = result.count;
            if (ctrl.publicationCount === 1) {
              RelatedPublicationSearchService
              .findByInstrumentId(ctrl.instrument.id, ['id', 'title'])
              .then(function(relatedPublication) {
                ctrl.relatedPublication = relatedPublication.
                hits.hits[0]._source;
              });
            }
          });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.instrument.id,
          'studyId': ctrl.instrument.studyId});
      });
    });
