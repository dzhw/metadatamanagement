'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, $state, StudySearchResource, SurveySearchDialogService,
      DataSetSearchDialogService, Language, DataSetSearchResource,
      SurveySearchService) {

      var ctrl = this;
      ctrl.survey = entity;
      ctrl.counts = {};
      entity.$promise.then(function(survey) {
        ctrl.survey = survey;
        StudySearchResource
        .findStudy(ctrl.survey.dataAcquisitionProjectId)
        .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        DataSetSearchResource
        .getCounts('surveyTitles',
        ctrl.survey.title[Language.getCurrentInstantly()])
        .then(function(dataSetsCount) {
              ctrl.counts.dataSetsCount = dataSetsCount.count;
            });
        SurveySearchService
          .countBy('dataAcquisitionProjectId',
          ctrl.survey.dataAcquisitionProjectId).then(function(surveysCount) {
              ctrl.counts.surveysCount = surveysCount.count - 1;
            });
      });
      ctrl.showStudy = function() {
        $state.go('studyDetail', {id: ctrl.survey.dataAcquisitionProjectId});
      };
      ctrl.showRelatedInstruments = function() {};
      ctrl.showRelatedDataSets = function() {
        DataSetSearchDialogService.findDataSets('findBySurveyTitle',
        ctrl.survey.title[Language.getCurrentInstantly()],
        ctrl.counts.dataSetsCount);
      };
      ctrl.showRelatedSurveys = function() {
        SurveySearchDialogService.findSurveys('findByProjectId',
        ctrl.survey.dataAcquisitionProjectId, ctrl.counts.surveysCount);
      };
    });
