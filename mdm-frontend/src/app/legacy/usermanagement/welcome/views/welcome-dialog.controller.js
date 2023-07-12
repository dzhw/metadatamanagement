'use strict';
angular.module('metadatamanagementApp')
  .controller('WelcomeDialogController',
  function($scope, $mdDialog, username, bowser, displayDeactivateDialogOption,
    currentLanguage) {
    $scope.bowser = bowser;
    $scope.username = username;
    $scope.displayDeactivateDialogOption =
      displayDeactivateDialogOption;
    $scope.language = currentLanguage;
    $scope.data = {
      doNotShowAgain: false
    };
    $scope.closeDialog = function() {
      $mdDialog.hide($scope.data.doNotShowAgain);
    };
  });
