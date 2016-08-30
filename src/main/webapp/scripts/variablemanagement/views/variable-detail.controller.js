'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService,
    blockUI, $scope, entity, $rootScope) {
    $scope.variable = entity;
    $scope.selected = [];
    $scope.reorder = function() {
      $scope.promise = $scope.variable.distribution.validResponses
    };
    $scope.query = {
      order: 'value',
      limit: 5,
      page: 1
    };
    $scope.$watch('variable', function() {
      if ($scope.variable.$resolved) {
        $scope.reorder();
        console.log($scope.variable.distribution.validResponses);
      }
    });
    console.log(entity);
    /* function to start blockUI */
    $scope.startBlockUI = function() {
      blockUI.start();
    };
    /* function to start blockUI */
    $scope.stopBlockUI = function() {
      blockUI.stop();
    };
    /* function to open dialog for variables */
    $scope.showSurveys = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.surveyIds, 'survey',
        ['label', 'name'], $rootScope.currentLanguage);
    };
    /* function to open dialog for data-sets */
    $scope.showDataSets = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.dataSetIds, 'data-set',
        ['label', 'name'], $rootScope.currentLanguage);
    };
  });
