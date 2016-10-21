'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableSearchDialogController',
    function($mdDialog, $scope, variables) {
      var ctrl = this;
      ctrl.variables = variables;
      ctrl.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctrl.closeDialog();
      });
    });
