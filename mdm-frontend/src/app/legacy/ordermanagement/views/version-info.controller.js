/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('VersionInfoController', [
  '$scope',
  '$mdDialog',
        function($scope, $mdDialog) {
          $scope.bowser = bowser;

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };
        }]);

