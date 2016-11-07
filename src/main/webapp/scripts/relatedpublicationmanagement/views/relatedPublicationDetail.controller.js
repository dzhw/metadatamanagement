'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController',
    function(entity, QuestionSearchDialogService, VariableSearchDialogService,
    SurveySearchDialogService, StudySearchDialogService,
    DataSetSearchDialogService) {

      var ctrl = this;
      ctrl.relatedPublication = entity;
      entity.$promise.then(function(relatedPublication) {
        ctrl.relatedPublication = relatedPublication;
      });
      ctrl.showStudies = function() {
        StudySearchDialogService
        .findStudies('findStudies', ctrl.relatedPublication.studyIds,
        ctrl.relatedPublication.studyIds.length);
      };
      ctrl.showQuestions = function() {
        QuestionSearchDialogService
        .findQuestions('findQuestions', ctrl.relatedPublication.questionIds,
        ctrl.relatedPublication.questionIds.length);
      };
      ctrl.showVariables = function() {
        VariableSearchDialogService
        .findVariables('findVariables', ctrl.relatedPublication.variableIds,
        ctrl.relatedPublication.variableIds.length);
      };
      ctrl.showSurveys = function() {
        SurveySearchDialogService
        .findSurveys('findSurveys', ctrl.relatedPublication.surveyIds,
        ctrl.relatedPublication.surveyIds.length);
      };
      ctrl.showDataSets = function() {
        DataSetSearchDialogService
          .findDataSets('findDataSets', ctrl.relatedPublication.dataSetIds,
        ctrl.relatedPublication.dataSetIds.length);
      };
    });
