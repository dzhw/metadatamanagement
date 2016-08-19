'use strict';

angular.module('metadatamanagementApp')
  .controller('DialogController',
  function($mdDialog, $scope, items) {
    $scope.items = items;
    $scope.closeDialog = $mdDialog.hide;
  });
