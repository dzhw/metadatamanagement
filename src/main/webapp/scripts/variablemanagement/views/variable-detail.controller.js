'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService, $translate,
    blockUI, $scope, $stateParams, $filter, entity) {

    $scope.variable = entity;
    $scope.frequencies = [];
    $scope.cleanedAccessWays = '';
    $scope.tableFlag = 'expand';

    $scope.$watch('variable', function() {
      if ($scope.variable.$resolved) {
        $scope.cleanedAccessWays = '' + $scope.variable.accessWays + '"';
        $scope.cleanedAccessWays = $scope.cleanedAccessWays
        .replace(/[\[\]'"]/g, '');
        console.log($scope.variable);
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
      }
    }, true);

    $scope.showRows = function() {
      if ($scope.frequencies.length > 8) {
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
    /* function to open dialog for variables */
    $scope.showSurveys = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.surveyIds, 'survey');
    };
    /* function to open dialog for data-sets */
    $scope.showDataSets = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.dataSetIds, 'data-set');
    };
    /* function to open dialog for similar variables */
    $scope.showSimilarVariables = function() {
      blockUI.start();
      DialogService.showDialog($scope.variable.sameVariablesInPanel,
        'variable');
    };
  });
