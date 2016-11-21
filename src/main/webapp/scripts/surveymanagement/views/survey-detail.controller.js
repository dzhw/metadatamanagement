'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, $state, StudySearchService, SurveySearchDialogService,
      DataSetSearchDialogService, LanguageService, DataSetSearchService,
      SurveySearchService) {

      var ctrl = this;
      ctrl.imgResolved = false;
      ctrl.survey = entity;
      ctrl.counts = {};
      entity.$promise.then(function() {
        StudySearchService
          .findStudy(ctrl.survey.dataAcquisitionProjectId)
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        DataSetSearchService
          .countBy('surveyIds',
            ctrl.survey.title[LanguageService.getCurrentInstantly()])
          .then(function(dataSetsCount) {
            ctrl.counts.dataSetsCount = dataSetsCount.count;
          });
        SurveySearchService
          .countBy('dataAcquisitionProjectId',
            ctrl.survey.dataAcquisitionProjectId, ctrl.survey.id)
          .then(function(surveysCount) {
            ctrl.counts.surveysCount = surveysCount.count;
          });
      });
      ctrl.showRelatedDataSets = function() {
        var paramObject = {};
        paramObject.methodName = 'findBySurveyId';
        paramObject.methodParams = ctrl.survey.id;
        DataSetSearchDialogService.findDataSets(paramObject);
      };
      ctrl.showRelatedSurveys = function() {
        var paramObject = {};
        paramObject.methodName = 'findByProjectId';
        paramObject.methodParams = ctrl.survey.dataAcquisitionProjectId;
        paramObject.surveyId = ctrl.survey.id;
        SurveySearchDialogService.findSurveys(paramObject);
      };
      ctrl.setImgResolved = function() {
        ctrl.imgResolved = true;
      };
    });
