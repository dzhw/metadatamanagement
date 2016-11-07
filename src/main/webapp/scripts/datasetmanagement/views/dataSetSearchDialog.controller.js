'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchDialogController',
    function($mdDialog, $scope, methodName, methodParams, count) {
      var DataSetSearchDialogController = this;
      DataSetSearchDialogController.methodName = methodName;
      DataSetSearchDialogController.methodParams = methodParams;
      DataSetSearchDialogController.count = count;
      DataSetSearchDialogController.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        DataSetSearchDialogController.closeDialog();
      });
    });
