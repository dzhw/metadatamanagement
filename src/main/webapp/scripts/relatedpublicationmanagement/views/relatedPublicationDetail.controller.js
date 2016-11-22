'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController',
    function(entity, QuestionSearchDialogService, VariableSearchDialogService,
    SurveySearchDialogService, StudySearchDialogService,
    DataSetSearchDialogService, InstrumentSearchDialogService) {

      var ctrl = this;
      ctrl.counts = {};
      ctrl.relatedPublication = entity;
      entity.$promise.then(function() {
        ctrl.counts.dataSetsCount = ctrl.relatedPublication.dataSetIds.length;
        ctrl.counts.questionsCount = ctrl.relatedPublication.questionIds.length;
        ctrl.counts.variablesCount = ctrl.relatedPublication.variableIds.length;
        ctrl.counts.surveysCount = ctrl.relatedPublication.surveyIds.length;
        ctrl.counts.studiesCount = ctrl.relatedPublication.studyIds.length;
        ctrl.counts.instrumentsCount = ctrl.relatedPublication
        .instrumentIds.length;
      });
      ctrl.showRelatedStudies = function() {
        var paramObject = {};
        paramObject.methodName = 'findStudies';
        paramObject.methodParams = ctrl.relatedPublication.studyIds;
        StudySearchDialogService
        .findStudies(paramObject);
      };
      ctrl.showRelatedQuestions = function() {
        var paramObject = {};
        paramObject.methodName = 'findQuestions';
        paramObject.methodParams = ctrl.relatedPublication.questionIds;
        QuestionSearchDialogService
        .findQuestions(paramObject);
      };
      ctrl.showRelatedVariables = function() {
        var paramObject = {};
        paramObject.methodName = 'findVariables';
        paramObject.methodParams = ctrl.relatedPublication.variableIds;
        VariableSearchDialogService
        .findVariables(paramObject);
      };
      ctrl.showRelatedSurveys = function() {
        var paramObject = {};
        paramObject.methodName = 'findSurveys';
        paramObject.methodParams = ctrl.relatedPublication.surveyIds;
        SurveySearchDialogService
        .findSurveys(paramObject);
      };
      ctrl.showRelatedDataSets = function() {
        var paramObject = {};
        console.log('hd');
        paramObject.methodName = 'findDataSets';
        paramObject.methodParams = ctrl.relatedPublication.dataSetIds;
        DataSetSearchDialogService
          .findDataSets(paramObject);
      };
      ctrl.showRelatedInstruments = function() {
        var paramObject = {};
        paramObject.methodName = 'findInstruments';
        paramObject.methodParams = ctrl.relatedPublication.instrumentIds;
        InstrumentSearchDialogService
          .findInstruments(paramObject);
      };
    });
