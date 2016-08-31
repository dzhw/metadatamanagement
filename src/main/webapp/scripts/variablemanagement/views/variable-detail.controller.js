'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService,
    blockUI, $scope, $stateParams, VariableResource, $rootScope) {
    /* paged frequencies */
    $scope.frequencies = [];
    /* all frequencies */
    $scope.allFrequencies = [];
    /* params for table */
    $scope.query = {
      order: 'value',
      limit: 5,
      count: 0,
      page: 1
    };
    /* see https://github.com/angular-ui/ui-router/issues/582 */
    VariableResource.get({id: $stateParams.id})
    .$promise.then(function(variable) {
      $scope.variable = variable;
      $scope.allFrequencies = $scope.variable.distribution.validResponses
      .concat($scope.variable.distribution.missings);
      $scope.query.count = $scope.allFrequencies.length;
      $scope.getStatistics();
    });
    /* function to update table */
    $scope.getStatistics = function() {
      var startPosition = ($scope.query.page - 1) * $scope.query.limit;
      var endPosition = startPosition + $scope.query.limit;
      $scope.frequencies = $scope.allFrequencies.slice(startPosition,
        endPosition);
    };
    /* function to start blockUI */
    $scope.startBlockUI = function() {
      blockUI.start();
    };
    /* function to stop blockUI */
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
