'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function($scope, entity, DataSetSearchResource, SurveySearchResource,
      ShoppingCartService, QuestionSearchDialogService, CleanJSObjectService,
      RelatedPublicationSearchDialogService, QuestionSearchResource,
      RelatedPublicationSearchResource) {
      $scope.study = entity;
      $scope.cleanedAccessWays = '';
      $scope.counts = {};

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
        QuestionSearchResource.getCounts('dataAcquisitionProjectId',
            $scope.study.id).then(function(questionsCount) {
                $scope.counts.questionsCount = questionsCount.count;
              });
        RelatedPublicationSearchResource.getCounts('studyIds',
            $scope.study.id).then(function(publicationsCount) {
                $scope.counts.publicationsCount = publicationsCount.count;
              });
      });
      $scope.showQuestions = function() {
        QuestionSearchDialogService
        .findQuestions('findByStudyId', $scope.study.id,
        $scope.counts.questionsCount);
      };
      $scope.showRelatedPublications = function() {
        RelatedPublicationSearchDialogService.
        findRelatedPublications('findByStudyId', $scope.study.id,
        $scope.counts.publicationsCount);
      };
      $scope.showInstruments = function() {};
      $scope.addToNotepad = function() {
        ShoppingCartService
        .addToShoppingCart($scope.study.id);
      };
    });
