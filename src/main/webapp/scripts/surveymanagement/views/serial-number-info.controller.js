/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('SerialNumberInfoController',
        function($scope, $mdDialog) {
          $scope.bowser = bowser;

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };
        });
