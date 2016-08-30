'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(DialogService,
    blockUI, $scope, entity, $rootScope) {
    $scope.variable = entity;
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
