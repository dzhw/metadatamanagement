/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('AccessWayInfoController',
        function($scope, $mdDialog) {
          $scope.bowser = bowser;

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };
        });
