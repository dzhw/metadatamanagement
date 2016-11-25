/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SimpleMessageToastController',
    function($scope, $mdToast, $mdDialog, messageId, messageParams) {
      $scope.messageId = messageId;
      $scope.messageParams = messageParams;

      $scope.$on('$serverError', function(event, data) {
        console.log('Hallo');
        console.log(event);
        console.log(data);
      });

      /* Close Function for Toasts. */
      $scope.closeToast = function() {
        $mdToast.hide();
      };
    });
