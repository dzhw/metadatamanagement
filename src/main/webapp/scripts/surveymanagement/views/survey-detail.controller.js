'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController',
    function(entity, VariableSearchDialogService, DataSetSearchDialogService,
      RelatedPublicationSearchDialogService, $state, $rootScope) {

      var ctrl = this;
      ctrl.survey = entity;
      entity.$promise.then(function(survey) {
        ctrl.survey = survey;
      });
      ctrl.showVariables = function() {
        VariableSearchDialogService
          .findBySurveyTitle(ctrl.survey.title[$rootScope.currentLanguage]);
      };
      ctrl.showStudy = function() {
        $state.go('studyDetail', {id: ctrl.survey.dataAcquisitionProjectId});
      };
      ctrl.showDataSets = function() {
        DataSetSearchDialogService
          .findBySurveyTitle(ctrl.survey.title[$rootScope.currentLanguage]);
      };
      ctrl.showRelatedPublications = function() {
        RelatedPublicationSearchDialogService.findBySurveyId(ctrl.survey.id);
      };
    });
