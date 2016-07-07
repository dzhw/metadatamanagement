/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('CreateProjectDialogController', function($scope, $mdDialog) {

    $scope.closeDialog = function() {
      $mdDialog.cancel();
    };

    $scope.answer = function(answer) {
      $mdDialog.hide(answer);
    };
  });
