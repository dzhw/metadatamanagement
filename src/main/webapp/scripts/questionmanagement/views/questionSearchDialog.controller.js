'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionSearchDialogController',
  function($mdDialog, $scope, methodName, methodParams, count) {
    var QuestionSearchDialogController = this;
    QuestionSearchDialogController.methodName = methodName;
    QuestionSearchDialogController.methodParams = methodParams;
    QuestionSearchDialogController.count = count;
    QuestionSearchDialogController.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      QuestionSearchDialogController.closeDialog();
    });
  });
