/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('JobCompleteToastController',
    function($scope, SynchronizedMdToast, $mdDialog, resultMessage,
      translationParams) {
      $scope.resultMessage = resultMessage;
      $scope.translationParams = translationParams;

      /* Close Function for Toasts. */
      $scope.closeToast = function() {
        SynchronizedMdToast.hide();
      };

      /* Dialog for the Log of Uploading data */
      $scope.showLog = function() {
        $mdDialog.show({
          controller: 'JobProtocolDialogController',
          templateUrl: 'scripts/common/joblogging/' +
            'views/job-protocol-dialog.html.tmpl',
          clickOutsideToClose: false
        });
        $scope.closeToast();
      };

    });
