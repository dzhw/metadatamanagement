'use strict';

angular.module('metadatamanagementApp').controller('BlockUIController', [
  '$scope',
  'JobLoggingService', function(
    $scope, JobLoggingService) {
    $scope.job = JobLoggingService.getCurrentJob();
  }]);

