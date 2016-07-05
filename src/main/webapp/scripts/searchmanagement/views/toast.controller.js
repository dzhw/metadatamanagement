/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SearchToastController',
  function($scope, $mdToast, $mdDialog, JobLoggingService) {

    $scope.job = JobLoggingService.getCurrentJob();

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
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

    $scope.$watch('job.state', function() {
      console.log($scope.job.state);
      if ($scope.job.state === 'finished') {
        $scope.$broadcast($scope.job.id +
          '-list-uploaded');
      }
    });
  });
