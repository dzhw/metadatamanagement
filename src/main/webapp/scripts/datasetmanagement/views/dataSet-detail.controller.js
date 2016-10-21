'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController', ['$scope', '$state', 'entity',
  'SurveySearchDialogService', 'VariableSearchDialogService',
  'ShoppingCartService', 'RelatedPublicationSearchDialogService',
    function($scope, $state, entity, SurveySearchDialogService,
    VariableSearchDialogService, ShoppingCartService,
    RelatedPublicationSearchDialogService) {
      $scope.allRowsVisible = true;
      entity.$promise.then(function(dataSet) {
        $scope.dataSet = dataSet;
      });
      $scope.isRowHidden = function(index) {
        if (index <= 4 || index >= $scope
          .dataSet.subDataSets.length - 5) {
          return false;
        } else {
          return $scope.allRowsVisible;
        }
      };
      $scope.toggleAllRowsVisible = function() {
        $scope.allRowsVisible = !$scope.allRowsVisible;
      };
      $scope.showSurveys = function() {
        SurveySearchDialogService.findSurveys($scope.dataSet.surveyIds);
      };
      $scope.showVariables = function() {
        VariableSearchDialogService.findVariables($scope.dataSet.variableIds);
      };
      $scope.showStudy = function() {
        $state.go('studyDetail', {id: $scope.dataSet.dataAcquisitionProjectId});
      };
      $scope.showRelatedPublications = function() {
        RelatedPublicationSearchDialogService
        .findByDataSetId($scope.dataSet.id);
      };
      /* add new  item to localStorage */
      $scope.addToNotepad = function() {
        ShoppingCartService
        .addToShoppingCart($scope.dataSet.dataAcquisitionProjectId);
      };
    }
  ]);
