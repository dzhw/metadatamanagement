/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('AccessWayInfoController', [
  '$scope',
  '$mdDialog',
        function($scope, $mdDialog) {
          $scope.bowser = bowser;

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };
        }]);

