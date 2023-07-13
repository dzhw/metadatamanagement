/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('SerialNumberInfoController', [
  '$scope',
  '$mdDialog',
        function($scope, $mdDialog) {
          $scope.bowser = bowser;

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };
        }]);

