/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('NoOpenProjectToastController',
  function($scope, $mdToast, $mdDialog, JobLoggingService, resultMessage) {

    /* Get the current job */
    $scope.job = JobLoggingService.getCurrentJob();
    $scope.resultMessage = resultMessage;

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
    };
  });
