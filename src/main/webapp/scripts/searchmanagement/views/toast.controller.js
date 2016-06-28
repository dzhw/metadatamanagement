/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SearchToastController',
  function($scope, $mdToast) {

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
    };

  });
