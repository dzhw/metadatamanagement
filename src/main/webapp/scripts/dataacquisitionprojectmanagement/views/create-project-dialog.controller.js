/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('CreateProjectDialogController', function($scope, $mdDialog) {

    /* Close Dialog without return value for hiding */
    $scope.closeDialog = function() {
      $mdDialog.cancel();
    };

    /* Hides the dialog and sends the project name the navbar controller for the
    button handling */
    $scope.ok = function(project) {
      $mdDialog.hide(project);
    };
  });
