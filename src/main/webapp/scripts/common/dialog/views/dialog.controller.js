'use strict';

angular.module('metadatamanagementApp')
  .controller('DialogController',
  function($mdDialog, $scope, $state, items, currentLanguage,
    VariableResource) {
    $scope.items = items;
    $scope.currentLanguage = currentLanguage;
    $scope.variables = [];
    var loadVariables = function() {
      items.forEach(function(variableId) {
        var resource = VariableResource.get({id: variableId});
        $scope.variables.push(resource);
      });
    };
    loadVariables();
    $scope.goToVariable = function(variable) {
      console.log(variable.id);
      $scope.closeDialog();
      $state.go(variable.id);
    };
    $scope.closeDialog = $mdDialog.hide;
  });
