'use strict';
angular.module('metadatamanagementApp').controller('FdzWelcomeDialogController',
  function($scope, $mdDialog, bowser) {
    $scope.bowser = bowser;
    $scope.closeDialog = function() {
      $mdDialog.hide();
    };
  });
