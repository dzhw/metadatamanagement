'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController',
    function($scope, entity, VariableExportService) {
      $scope.variable = entity;

      $scope.exportToODT = function() {
        VariableExportService.exportToODT($scope.variable);
      };
    });
