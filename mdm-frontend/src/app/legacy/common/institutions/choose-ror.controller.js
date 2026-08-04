/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChooseRORController', [
  'nameEn', 'nameDe',
  'rorResponse',
  '$mdDialog',
  'LanguageService',
  '$scope',
    function(nameEn, nameDe, rorResponse, $mdDialog,
             LanguageService, $scope) {
      $scope.bowser = bowser;
      $scope.nameEn = nameEn;
      $scope.nameDe = nameDe;
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

