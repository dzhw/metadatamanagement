'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService, $translate,
    blockUI, $scope, $stateParams, $filter, VariableResource, $rootScope) {
    /* paged frequencies */
    $scope.frequencies = [];
    /* all frequencies */
    $scope.allFrequencies = [];
    /* params for table */
    $scope.query = {
      order: 'value',
      limit: 5,
      count: 0,
      allLabel: {},
      page: 1
    };
    /* options for pager */
    $scope.options = {};

    /* see https://github.com/angular-ui/ui-router/issues/582 */
    VariableResource.get({id: $stateParams.id})
    .$promise.then(function(variable) {
      $scope.variable = variable;
      console.log(variable);
      $scope.allFrequencies = $scope.variable.distribution.validResponses
      .concat($scope.variable.distribution.missings);
      $scope.query.count = $scope.allFrequencies.length;
      var tempLimitOptions = [];
      if ($scope.allFrequencies.length === $scope.query.limit) {
        tempLimitOptions = [$scope.query.limit];
      }else {
        tempLimitOptions = [5, 10, {
            label: $translate.instant('variable-management.detail.label.all'),
            value: function() {
                return $scope.allFrequencies.length;
              }
          }];
      }
      $scope.options = {
        boundaryLinks: true,
        pageSelect: true,
        label: {
          page: $translate.instant('variable-management.detail.label.page'),
          rowsPerPage:
           $translate.instant('variable-management.detail.label.rowsPerPage'),
          of: $translate.instant('variable-management.detail.label.of')
        },
        limitOptions: tempLimitOptions
      };
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
