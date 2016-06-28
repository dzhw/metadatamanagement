/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SearchToastController',
  function($scope, $mdToast, $mdDialog) {

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
    };

    /* Dialog for the Log of Uploading data */
    $scope.showLog = function() {
      $mdDialog.show(
        $mdDialog.alert()
        .clickOutsideToClose(true)
        .title('This is an log title')
        .textContent(
          'The Log has to be implemented')
        .ariaLabel('The Log has to be implemented')
        .ok('Ok... Do it now! ;)')
      );
    };
  });
