'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController',
    function($scope, $rootScope, $stateParams, entity, Variable,
      VariableExportService) {
      $scope.variable = entity;
      $scope.load = function(id) {
        Variable.get({id: id}, function(result) {
          $scope.variable = result;
        });
      };
      $scope.exportToODT = function() {
        VariableExportService.exportToODT($scope.variable);
      };
      var unsubscribe = $rootScope.$on('metadatamanagementApp:variableUpdate',
      function(event, result) {
        $scope.variable = result;
      });
      $scope.$on('$destroy', unsubscribe);

    });
