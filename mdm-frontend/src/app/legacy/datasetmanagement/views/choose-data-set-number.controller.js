/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChooseDataSetNumberController',
    function(availableDataSetNumbers, $scope, $mdDialog) {
      $scope.bowser = bowser;
      $scope.availableDataSetNumbers = availableDataSetNumbers;
      $scope.selectedDataSetNumber =
        availableDataSetNumbers[0];

      $scope.closeDialog = function() {
        $mdDialog.hide({
          dataSetNumber: $scope.selectedDataSetNumber
        });
      };
    });
