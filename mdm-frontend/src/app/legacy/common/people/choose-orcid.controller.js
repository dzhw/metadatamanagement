/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChooseORCIDController', [
  'firstName',
  'lastName',
  'orcidResponse',
  '$mdDialog',
  'LanguageService',
  '$scope',
    function(firstName, lastName, orcidResponse, $mdDialog,
             LanguageService, $scope) {
      $scope.bowser = bowser;
      $scope.firstName = firstName;
      $scope.lastName = lastName;
      $scope.orcidResponse = orcidResponse;

      $scope.currentLanguage = LanguageService.getCurrentInstantly();

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(orcid) {
        $mdDialog.hide({
          orcid: orcid
        });
      };
    }]);

