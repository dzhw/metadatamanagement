/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SimpleMessageToastController',
  function($scope, $mdToast, $mdDialog, messageId, messageParam) {
    $scope.messageId = messageId;
    $scope.messageParam = messageParam;

    /* Close Function for Toasts. */
    $scope.closeToast = function() {
      $mdToast.hide();
    };
  });
