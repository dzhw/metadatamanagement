/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SimpleMessageToastController',
    function($scope, $mdToast, messageId, messageParams, alert) {
      $scope.messageId = messageId;
      $scope.messageParams = messageParams;
      $scope.alert = alert;

      /* Close Function for Toasts. */
      $scope.closeToast = function() {
        $mdToast.hide();
      };
    });
