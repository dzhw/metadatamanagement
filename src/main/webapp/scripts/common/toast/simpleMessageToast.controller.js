/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SimpleMessageToastController',
    function($scope, $mdToast, messages, alert) {
      $scope.messages = messages;
      $scope.alert = alert;

      /* Close Function for Toasts. */
      $scope.closeToast = function() {
        $mdToast.hide();
      };
    });
