'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableSearchDialogController',
    function($mdDialog, $scope, methodName, methodParams, count) {
      var VariableSearchDialogController = this;
      VariableSearchDialogController.methodName = methodName;
      VariableSearchDialogController.methodParams = methodParams;
      VariableSearchDialogController.count = count;
      VariableSearchDialogController.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        VariableSearchDialogController.closeDialog();
      });
    });
