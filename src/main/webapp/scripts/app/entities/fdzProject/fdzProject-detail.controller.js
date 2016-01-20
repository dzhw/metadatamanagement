'use strict';

angular.module('metadatamanagementApp')
    .controller('FdzProjectDetailController',
      function($scope, $rootScope, $stateParams,
        entity, FdzProjectExportService, VariableCollection) {
        $scope.fdzProject = entity;

        $scope.loadAll = function() {
          VariableCollection.query($scope.fdzProject.id,
            function(result) {
              $scope.totalItems = result.page.totalElements;
              $scope.variables = result._embedded.variables;
            });
        };
        $scope.loadAll();

        $scope.exportToODT = function() {
          FdzProjectExportService.exportToODT($scope.variables);
        };
      });
