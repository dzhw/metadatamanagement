'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchDialogController',
    function($mdDialog, $scope, methodName, methodParams) {
      var DataSetSearchDialogController = this;
      DataSetSearchDialogController.methodName = methodName;
      DataSetSearchDialogController.methodParams = methodParams;
      DataSetSearchDialogController.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        DataSetSearchDialogController.closeDialog();
      });
    });
