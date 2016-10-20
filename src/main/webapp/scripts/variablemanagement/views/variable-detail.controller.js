'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(SurveySearchDialogService,
    DataSetSearchDialogService, $scope, entity, $state, ShoppingCartService) {
    $scope.generationCodeToggleFlag = true;
    $scope.notAllRowsVisible = true;
    entity.$promise.then(function(variable) {
      $scope.variable = variable;
    });
    $scope.isRowHidden = function(index) {
      if (index <= 4 || index >= $scope
        .variable.distribution.validResponses.length - 5) {
        return false;
      } else {
        return $scope.notAllRowsVisible;
      }
    };
    $scope.toggleAllRowsVisible = function() {
      $scope.notAllRowsVisible = !$scope.notAllRowsVisible;
    };
    $scope.toggleGenerationCode = function() {
      $scope.generationCodeToggleFlag = !$scope.generationCodeToggleFlag;
    };
    $scope.showSurveys = function() {
      SurveySearchDialogService.findSurveys($scope.variable.surveyIds);
    };
    $scope.showDataSets = function() {
      DataSetSearchDialogService.findByVariableId($scope.variable.id);
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
