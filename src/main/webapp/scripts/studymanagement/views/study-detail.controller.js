'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function($scope, entity, DataSetSearchResource, SurveySearchResource,
      ShoppingCartService, QuestionSearchDialogService, CleanJSObjectService) {
      $scope.study = entity;
      $scope.cleanedAccessWays = '';

      entity.$promise.then(function(study) {
        $scope.study = study;
        SurveySearchResource.findByProjectId($scope.study.id)
        .then(function(surveys) {
          if (!CleanJSObjectService.isNullOrEmpty(surveys)) {
            $scope.surveys = surveys.hits.hits;
          }
        });
        DataSetSearchResource.findByProjectId($scope.study.id)
        .then(function(dataSets) {
          if (!CleanJSObjectService.isNullOrEmpty(dataSets)) {
            $scope.dataSets = dataSets.hits.hits;
          }
        });
      });
      $scope.showQuestions = function() {
        QuestionSearchDialogService
        .findByProjectId($scope.study.dataAcquisitionProjectId);
      };
      $scope.showInstruments = function() {};
      $scope.showRelatedPublication = function() {};
      $scope.addToNotepad = function() {
        ShoppingCartService
        .addToShoppingCart($scope.study.id);
      };
    });
