'use strict';

angular.module('metadatamanagementApp')
    .controller('FdzProjectDetailController',
      function($scope, $rootScope, $stateParams,
        entity, FdzProjectExportService) {
        $scope.fdzProject = entity;

        $scope.exportToODT = function() {
          console.log($scope.variables);
          console.log($scope.fdzProject.name);
          FdzProjectExportService.exportToODT($scope.fdzProject);
        };
      });
