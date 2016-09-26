'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchDialogController',
  function($mdDialog, $scope, dataSets) {
    $scope.dataSets = dataSets;
    $scope.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      $scope.closeDialog();
    });
  });
