/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SimpleMessageToastController',
    function($scope, $mdToast, messageId, messageParams) {
      $scope.messageId = messageId;
      $scope.messageParams = messageParams;

      /* Close Function for Toasts. */
      $scope.closeToast = function() {
        $mdToast.hide();
      };
    });
