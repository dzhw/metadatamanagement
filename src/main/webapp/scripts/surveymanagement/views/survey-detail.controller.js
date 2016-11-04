'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, $state, StudySearchResource, SurveySearchDialogService,
      DataSetSearchDialogService, $rootScope, DataSetSearchResource,
      SurveySearchResource) {

      var ctrl = this;
      ctrl.survey = entity;
      ctrl.counts = {};
      entity.$promise.then(function(survey) {
        ctrl.survey = survey;
        StudySearchResource
        .findStudy(ctrl.survey.dataAcquisitionProjectId).then(function(study) {
            ctrl.study = study.hits.hits[0]._source;
          });
        DataSetSearchResource
        .getCounts('surveyTitles',
        ctrl.survey.title[$rootScope.currentLanguage])
        .then(function(dataSetsCount) {
              ctrl.counts.dataSetsCount = dataSetsCount.count;
            });
        SurveySearchResource
          .getCounts('dataAcquisitionProjectId',
          ctrl.survey.dataAcquisitionProjectId).then(function(surveysCount) {
              ctrl.counts.surveysCount = surveysCount.count;
            });
      });
      ctrl.showStudy = function() {
        $state.go('studyDetail', {id: ctrl.survey.dataAcquisitionProjectId});
      };
      ctrl.showRelatedInstruments = function() {};
      ctrl.showRelatedDataSets = function() {
        DataSetSearchDialogService.findDataSets('findBySurveyTitle',
        ctrl.survey.title[$rootScope.currentLanguage]);
      };
      ctrl.showRelatedSurveys = function() {
        SurveySearchDialogService.findSurveys('findByProjectId',
        ctrl.survey.dataAcquisitionProjectId, ctrl.counts.surveysCount);
      };
    });
