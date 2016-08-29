'use strict';

angular.module('metadatamanagementApp')
  .controller('DialogController',
  function($mdDialog, $scope, $state, entities, type, currentLanguage) {
    $scope.entities = entities;
    $scope.type = type;
    $scope.currentLanguage = currentLanguage;

    /* function to change the state to object state */
    $scope.goToEntity = function(type, params) {
      $scope.closeDialog();
      $state.go(type, params);
    };
    $scope.closeDialog = $mdDialog.hide;
  });
