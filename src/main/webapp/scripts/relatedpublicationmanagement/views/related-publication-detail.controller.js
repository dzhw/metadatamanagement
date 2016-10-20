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
        .findStudies(ctrl.relatedPublication.studyIds);
      };
      ctrl.showQuestions = function() {
        QuestionSearchDialogService
        .findQuestions(ctrl.relatedPublication.questionIds);
      };
      ctrl.showVariables = function() {
        VariableSearchDialogService
          .findVariables(ctrl.relatedPublication.variableIds);
      };
      ctrl.showSurveys = function() {
        SurveySearchDialogService
          .findSurveys(ctrl.relatedPublication.surveyIds);
      };
      ctrl.showDataSets = function() {
        DataSetSearchDialogService
          .findDataSets(ctrl.relatedPublication.dataSetIds);
      };
    });
