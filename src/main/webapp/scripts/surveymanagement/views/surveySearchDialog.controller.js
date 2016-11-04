'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySearchDialogController',
    function($mdDialog, $scope, methodName, methodParams, count) {
      var SurveySearchDialogController = this;
      SurveySearchDialogController.methodName = methodName;
      SurveySearchDialogController.methodParams = methodParams;
      SurveySearchDialogController.count = count;
      SurveySearchDialogController.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        SurveySearchDialogController.closeDialog();
      });
    });
