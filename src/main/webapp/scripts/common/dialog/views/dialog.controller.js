'use strict';

angular.module('metadatamanagementApp')
  .controller('DialogController',
  function(blockUI, $mdDialog, $scope, $state, $resource, variables,
    currentLanguage) {
    $scope.variables = [];
    $scope.counter = 0;
    $scope.currentLanguage = currentLanguage;
    var loadVariables = function() {
      if ($scope.counter < variables.length) {
        $resource('api/variables/:id')
        .get({id: variables[$scope.counter],
          projection: 'complete'})
        .$promise.then(function(resource) {
          $scope.variables.push(resource);
          $scope.counter++;
          loadVariables();
        }, function() {
          var notFoundVariable = {
            id: variables[$scope.counter],
            name: 'notFoundVariable'
          };
          $scope.variables.push(notFoundVariable);
          $scope.counter++;
          loadVariables();
        });
      }
    };
    $scope.$watch('counter', function() {
      var tempSum = $scope.counter;
      if ((variables.length === tempSum) && (tempSum > 0)) {
        blockUI.stop();
        $scope.finished = false;
      }
    }, true);
    loadVariables();
    $scope.goToVariable = function(variable) {
      $scope.closeDialog();
      $state.go(variable.id);
    };
    $scope.closeDialog = $mdDialog.hide;
  });
