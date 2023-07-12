/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('VersionInfoController',
        function($scope, $mdDialog) {
          $scope.bowser = bowser;

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };
        });
