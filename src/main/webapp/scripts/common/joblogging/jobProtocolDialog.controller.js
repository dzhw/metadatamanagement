/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller(
  'JobProtocolDialogController', function(
    $scope, $mdDialog, JobLoggingService) {
    $scope.job = JobLoggingService.getCurrentJob();
    $scope.closeDialog = function() {
      $mdDialog.hide();
    };
  });
