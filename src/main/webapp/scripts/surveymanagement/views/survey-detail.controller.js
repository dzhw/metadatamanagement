'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, StudySearchService, LanguageService, DataSetSearchService,
      SurveySearchService, PageTitleService, InstrumentSearchService,
      RelatedPublicationSearchService, $state, ToolbarHeaderService,
      SurveyAttachmentResource) {
      var ctrl = this;
      ctrl.imgResolved = false;
      ctrl.survey = entity;
      ctrl.counts = {};
      entity.$promise.then(function() {
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('survey-management.detail.title', {
          title: ctrl.survey.title[currenLanguage] ? ctrl
          .survey.title[currenLanguage] : ctrl.survey.title[secondLanguage],
          surveyId: ctrl.survey.id
        });
        StudySearchService.findOneByProjectId(ctrl.survey.
          dataAcquisitionProjectId, ['dataAcquisitionProjectId', 'title', 'id'])
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        DataSetSearchService.countBy('surveyIds', ctrl.survey.id)
          .then(function(dataSetsCount) {
            ctrl.counts.dataSetsCount = dataSetsCount.count;
            if (dataSetsCount.count === 1) {
              DataSetSearchService.findBySurveyId(ctrl.survey.id,
              ['description', 'id'])
              .then(function(dataSet) {
                ctrl.dataSet = dataSet.hits.hits[0]._source;
              });
            }
          });
        SurveySearchService.countBy('dataAcquisitionProjectId',
            ctrl.survey.dataAcquisitionProjectId)
          .then(function(surveysCount) {
            ctrl.counts.surveysCount = surveysCount.count;
          });
        InstrumentSearchService.countBy('surveyIds', ctrl.survey.id)
          .then(function(instrumentsCount) {
            ctrl.counts.instrumentsCount = instrumentsCount.count;
            if (instrumentsCount.count === 1) {
              InstrumentSearchService.findBySurveyId(ctrl.survey.id,
                ['dataAcquisitionProjectId', 'number', 'title', 'id',
              'description'])
              .then(function(instrument) {
                ctrl.instrument = instrument.hits.hits[0]._source;
              });
            }
          });
        SurveyAttachmentResource.findBySurveyId({
            id: ctrl.survey.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
        RelatedPublicationSearchService.countBy('surveyIds', ctrl.survey.id)
            .then(function(publicationsCount) {
              ctrl.counts.publicationsCount = publicationsCount.count;
              if (publicationsCount.count === 1) {
                RelatedPublicationSearchService
                  .findBySurveyId(ctrl.survey.id, ['id', 'title'])
                  .then(function(relatedPublication) {
                    ctrl.relatedPublication = relatedPublication.
                    hits.hits[0]._source;
                  });
              }
            });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'number': ctrl.survey.number,
          'studyId': ctrl.survey.studyId,
          'projectId': ctrl.survey.dataAcquisitionProjectId});
      });
      ctrl.setImgResolved = function() {
        ctrl.imgResolved = true;
      };
    });
