/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SearchToastController',
  function($scope, $mdToast, $mdDialog, JobLoggingService) {

    /* Get the current job */
    $scope.job = JobLoggingService.getCurrentJob();

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
    };

    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    /* Dialog for the Log of Uploading data */
    $scope.showLog = function() {
      $mdDialog.show({
        controller: function() { this.parent = $scope; },
        controllerAs: 'ctrl',
        templateUrl: 'scripts/searchmanagement/views/dialog-log.html.tmpl',
        clickOutsideToClose: true
      });
    };


  });
