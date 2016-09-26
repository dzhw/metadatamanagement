'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableSearchDialogController',
  function($mdDialog, $scope, variables) {
    $scope.variables = variables;
    $scope.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      $scope.closeDialog();
    });
  });
