'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService,
    blockUI, $scope, entity, $rootScope) {
    $scope.variable = entity;
    $scope.frequencies = [];
    $scope.allFrequencies = [];
    $scope.query = {
      order: 'value',
      limit: 5,
      count: 0,
      page: 1
    };
    $scope.$watch('variable', function() {
      if ($scope.variable.$resolved) {
        console.log(entity);
        $scope.allFrequencies = $scope.variable.distribution.validResponses
        .concat($scope.variable.distribution.missings);
        $scope.query.count = $scope.allFrequencies.length;
        $scope.getStatistics();
      }
    });
    $scope.getStatistics = function() {
      console.log($scope.query);
      var startPosition = ($scope.query.page - 1) * $scope.query.limit;
      var endPosition = startPosition + $scope.query.limit;
      $scope.frequencies = $scope.allFrequencies.slice(startPosition,
        endPosition);
    };
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
