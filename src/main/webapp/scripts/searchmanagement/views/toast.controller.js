/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SearchToastController',
  function($scope, $mdToast, $mdDialog, JobLoggingService, resultMessage) {

    /* Get the current job */
    $scope.job = JobLoggingService.getCurrentJob();
    $scope.resultMessage = resultMessage;

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
    };

    /* Close Function for Dialogs. */
    $scope.closeDialog = function() {
      $mdDialog.cancel();
    };

    /* Dialog for the Log of Uploading data */
    $scope.showLog = function() {
      $mdDialog.show({
        controller: function() { this.parent = $scope; },
        controllerAs: 'searchToastController',
        templateUrl: 'scripts/searchmanagement/views/dialog-log.html.tmpl',
        clickOutsideToClose: true
      });
    };

  });
