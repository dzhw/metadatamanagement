'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService, $translate,
    blockUI, $scope, $stateParams, $filter, VariableResource, $rootScope) {
    /* paged frequencies */
    $scope.frequencies = [];
    /* all frequencies */
    $scope.allFrequencies = [];

    $scope.tableFlag = 'expand';
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
      if ($scope.variable.distribution.validResponses) {
        $scope.frequencies = $scope.variable.distribution.validResponses
        .concat($scope.variable.distribution.missings);
        if ($scope.frequencies.length > 8) {
          $scope.frequencies.splice(3, 0, {
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

    $scope.showRows = function() {
      if($scope.frequencies.length > 8) {
        if ($scope.tableFlag === 'expand') {
          var hiddenRows = angular.element('.ng-hide');
          $scope.tableFlag = 'collapse';
          for (var i = 0; i < hiddenRows.length; i++) {
            angular.element('#' + hiddenRows[i]
            .getAttribute('id')).addClass('ng-show').removeClass('ng-hide');
          }
          angular.element('#row3').addClass('ng-hide').removeClass('ng-show');
        }else {
          var displayedRows = angular.element('.ng-show');
          $scope.tableFlag = 'expand';
          for (var j = 0; j < displayedRows.length; j++) {
            angular.element('#' + displayedRows[j]
            .getAttribute('id')).addClass('ng-hide').removeClass('ng-show');
          }
          angular.element('#row3').addClass('ng-show').removeClass('ng-hide');
        }
      }
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
