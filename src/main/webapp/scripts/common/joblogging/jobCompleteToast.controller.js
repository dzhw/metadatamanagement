/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('JobCompleteToastController',
  function($scope, $mdToast, $mdDialog, resultMessage) {
    $scope.resultMessage = resultMessage;

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
    };

    /* Dialog for the Log of Uploading data */
    $scope.showLog = function() {
      $mdDialog.show({
        controller: 'JobProtocolDialogController',
        templateUrl: 'scripts/common/joblogging/job-protocol-dialog.html.tmpl',
        clickOutsideToClose: true
      });
      $scope.closeToast();
    };

  });
