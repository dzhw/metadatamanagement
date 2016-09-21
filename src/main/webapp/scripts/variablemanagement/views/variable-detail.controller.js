'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService, blockUI,
    $scope, entity, $state, ShoppingCartService) {
    $scope.frequencies = [];
    $scope.generationCodeToggleFlag = true;
    $scope.allRowsVisible = true;
    entity.$promise.then(function(variable) {
      $scope.variable = variable;
    });
    $scope.isRowHidden = function(index) {
      if (index <= 4 || index >= $scope
        .variable.distribution.validResponses.length - 5) {
        return false;
      } else {
        return $scope.allRowsVisible;
      }
    };
    $scope.toggleAllRowsVisible = function() {
      $scope.allRowsVisible = !$scope.allRowsVisible;
    };
    $scope.toggleGenerationCode = function() {
      $scope.generationCodeToggleFlag = !$scope.generationCodeToggleFlag;
    };
    /* function to open dialog for variables */
    $scope.showSurveys = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.surveyIds, 'survey');
    };
    /* function to open dialog for data-sets */
    $scope.showDataSets = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.id, 'data-set');
    };
    /* function to open dialog for similar variables */
    $scope.showSimilarVariables = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.sameVariablesInPanel,
        'variable');
    };
    $scope.showStudy = function() {
      $state.go('studyDetail', {id: $scope.variable.dataAcquisitionProjectId});
    };
    /* add new  item to localStorage */
    $scope.addToNotepad = function() {
      ShoppingCartService
      .addToShoppingCart($scope.variable.dataAcquisitionProjectId);
    };
  });
