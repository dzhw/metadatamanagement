'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySearchDialogController',
    function($mdDialog, $scope, methodName, methodParams, count, surveyId) {
      var ctrl = this;
      ctrl.methodName = methodName;
      ctrl.methodParams = methodParams;
      ctrl.count = count;
      ctrl.surveyId = surveyId;
      ctrl.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctrl.closeDialog();
      });
    });
