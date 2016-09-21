'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService, blockUI,
    $scope, entity, $state, ShoppingCartService) {
    $scope.frequencies = [];
    $scope.generationCodeToggleFlag = true;
    $scope.toggleRowsFlag = true;
    entity.$promise.then(function(variable) {
      $scope.variable = variable;
      if ($scope.variable.distribution.validResponses) {
        $scope.frequencies = $scope.variable.distribution.validResponses
        .concat($scope.variable.distribution.missings);
        if ($scope.frequencies.length > 10) {
          $scope.frequencies.splice(5, 0, {
            value: '...',
            label: {
              de: '...',
              en: '...'
            },
            absoluteFrequency: '...',
            validRelativeFrequency: '...',
            relativeFrequency: '...'
          });
        }
      } else {
        $scope.frequencies = $scope.variable.distribution.missings;
      }
    });
    $scope.rowToggleCondition = function(index) {
      if ((index >= 6) && (index < ($scope.frequencies.length - 5)) &&
      $scope.toggleRowsFlag) {
        return true;
      }
      if (index === 5 && !$scope.toggleRowsFlag) {
        return true;
      }
      return false;
    };
    $scope.toggleRows = function() {
      $scope.toggleRowsFlag = !$scope.toggleRowsFlag;
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
