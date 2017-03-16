'use strict';

angular.module('metadatamanagementApp')
  .controller('ReleaseProjectDialogController', function($scope, $mdDialog,
    project) {
    $scope.project = project;
    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    $scope.ok = function(release) {
      release.date = new Date().toISOString();
      $mdDialog.hide(release);
    };
  });
