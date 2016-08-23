'use strict';

angular.module('metadatamanagementApp')
  .controller('DialogController',
  function($mdDialog, $scope, $state, variables, currentLanguage) {
    $scope.variables = variables;
    $scope.currentLanguage = currentLanguage;
    $scope.goToVariable = function(variable) {
      console.log(variable.id);
      $scope.closeDialog();
      $state.go(variable.id);
    };
    $scope.closeDialog = $mdDialog.hide;
  });
