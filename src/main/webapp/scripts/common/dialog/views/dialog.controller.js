'use strict';

angular.module('metadatamanagementApp')
  .controller('DialogController',
  function($mdDialog, $scope, $state, variables, currentLanguage) {
    $scope.variables = variables;
    $scope.currentLanguage = currentLanguage;

    /* function to change the state to variable state */
    $scope.goToVariable = function(variable) {
      $scope.closeDialog();
      $state.go(variable.id);
    };
    $scope.closeDialog = $mdDialog.hide;
  });
