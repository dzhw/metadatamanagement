'use strict';

angular.module('metadatamanagementApp')
  .controller('UserMessageDialogController', function($scope, $mdDialog,
    WebSocketService, $rootScope) {
    $scope.bowser = $rootScope.bowser;
    $scope.message = {};

    $scope.closeDialog = function() {
      $mdDialog.cancel();
    };

    /* Hides the dialog and sends the project name the navbar controller for the
    button handling */
    $scope.sendMessageToAllUsers = function() {
      WebSocketService.sendMessageToAllUsers($scope.message);
    };
  });
