/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('dataPackageInfoController',
        function($scope, $mdDialog) {
          $scope.bowser = bowser;

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };
        });
