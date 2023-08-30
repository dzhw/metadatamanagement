/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChooseInstrumentNumberController', [
  'availableInstrumentNumbers',
  '$scope',
  '$mdDialog',
    function(availableInstrumentNumbers, $scope, $mdDialog) {
      $scope.bowser = bowser;
      $scope.availableInstrumentNumbers = availableInstrumentNumbers;
      $scope.selectedInstrumentNumber =
        availableInstrumentNumbers[0];

      $scope.closeDialog = function() {
        $mdDialog.hide({
          instrumentNumber: $scope.selectedInstrumentNumber
        });
      };
    }]);

