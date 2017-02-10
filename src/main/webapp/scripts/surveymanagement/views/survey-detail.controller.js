'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, StudySearchService, LanguageService, DataSetSearchService,
      SurveySearchService, PageTitleService, InstrumentSearchService) {
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
        StudySearchService
          .findStudy(ctrl.survey.dataAcquisitionProjectId)
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        DataSetSearchService.countBy('surveyIds', ctrl.survey.id)
          .then(function(dataSetsCount) {
            ctrl.counts.dataSetsCount = dataSetsCount.count;
          });
        SurveySearchService.countBy('dataAcquisitionProjectId',
            ctrl.survey.dataAcquisitionProjectId)
          .then(function(surveysCount) {
            ctrl.counts.surveysCount = surveysCount.count;
          });
        InstrumentSearchService.countBy('surveyIds', ctrl.survey.id)
          .then(function(instrumentsCount) {
            ctrl.counts.instrumentsCount = instrumentsCount.count;
          });
      });
      ctrl.setImgResolved = function() {
        ctrl.imgResolved = true;
      };
    });
