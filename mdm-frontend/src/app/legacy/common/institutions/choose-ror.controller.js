/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChooseRORController', [
  'name',
  'rorResponse',
  '$mdDialog',
  'LanguageService',
  '$scope',
    function(name, rorResponse, $mdDialog,
             LanguageService, $scope) {
      $scope.bowser = bowser;
      $scope.name = name;
      $scope.rorResponse = rorResponse;

      $scope.currentLanguage = LanguageService.getCurrentInstantly();

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(ror) {
        $mdDialog.hide({
          ror: ror
        });
      };
    }]);

