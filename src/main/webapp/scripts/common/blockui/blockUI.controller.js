'use strict';

angular.module('metadatamanagementApp').controller(
  'BlockUIController', function(
    $scope, JobLoggingService) {
    $scope.job = JobLoggingService.getCurrentJob();
  });
