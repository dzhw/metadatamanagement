'use strict';
angular.module('metadatamanagementApp').controller('FdzWelcomeDialogController',
  function($scope, $mdDialog, localStorageService, bowser) {
    $scope.bowser = bowser;
    $scope.closeWelcomeDialogForever = false;

    $scope.closeDialog = function() {
      if ($scope.closeWelcomeDialogForever) {
        localStorageService.set('closeWelcomeDialogForever', true);
      }
      $mdDialog.hide();
    };
  });
