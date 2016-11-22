'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController',
  function(entity, Principal, StudySearchService,
    SurveySearchDialogService, VariableSearchDialogService,
    VariableSearchService, RelatedPublicationSearchService,
    RelatedPublicationSearchDialogService, DataSetReportService) {
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.allRowsVisible = true;
      ctrl.counts = {};
      ctrl.dataSet = entity;
      ctrl.counts = {};
      entity.$promise.then(function() {
        StudySearchService.findStudy(ctrl.dataSet.dataAcquisitionProjectId)
        .then(function(study) {
          if (study.hits.hits.length > 0) {
            ctrl.study = study.hits.hits[0]._source;
          }
        });
        RelatedPublicationSearchService.countBy('dataSetIds',
        ctrl.dataSet.id).then(function(publicationsCount) {
          ctrl.counts.publicationsCount = publicationsCount.count;
        });
        ctrl.counts.surveysCount = ctrl.dataSet.surveyIds.length;
        ctrl.counts.variablesCount = ctrl.dataSet.variableIds.length;
      });
      ctrl.uploadTexTemplate = function(file, dataSetId) {
        DataSetReportService.uploadTexTemplate(file, dataSetId);
      };
      ctrl.isRowHidden = function(index) {
        if (index <= 4 || index >= ctrl
          .dataSet.subDataSets.length - 5) {
          return false;
        } else {
          return ctrl.allRowsVisible;
        }
      };
      ctrl.toggleAllRowsVisible = function() {
        ctrl.allRowsVisible = !ctrl.allRowsVisible;
      };
      ctrl.showRelatedSurveys = function() {
        var paramObject = {};
        paramObject.methodName = 'findSurveys';
        paramObject.methodParams =   ctrl.dataSet.surveyIds;
        SurveySearchDialogService.findSurveys(paramObject);
      };
      ctrl.showRelatedVariables = function() {
        var paramObject = {};
        paramObject.methodName = 'findVariables';
        paramObject.methodParams =   ctrl.dataSet.variableIds;
        VariableSearchDialogService.findVariables(paramObject);
      };
      ctrl.showRelatedPublications = function() {
        var paramObject = {};
        paramObject.methodName = 'findByDataSetId';
        paramObject.methodParams =   ctrl.dataSet.id;
        RelatedPublicationSearchDialogService.
        findRelatedPublications(paramObject);
      };
    });
