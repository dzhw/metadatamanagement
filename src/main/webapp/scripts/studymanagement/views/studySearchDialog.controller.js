'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySearchDialogController',
  function($mdDialog, $scope, methodName, methodParams, count) {
    var StudySearchDialogController = this;
    StudySearchDialogController.methodName = methodName;
    StudySearchDialogController.methodParams = methodParams;
    StudySearchDialogController.count = count;
    StudySearchDialogController.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      StudySearchDialogController.closeDialog();
    });
  });
