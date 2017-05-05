'use strict';
angular.module('metadatamanagementApp').controller('FdzWelcomeDialogController',
  function($scope, $mdDialog) {
          $scope.closeDialog = function() {
            $mdDialog.hide();
          };
        });
