'use strict';

angular.module('metadatamanagementApp')
    .controller('FdzProjectDetailController',
      function($scope, $rootScope, $stateParams,
        entity, FdzProjectExportService) {
        $scope.fdzProject = entity;

        $scope.exportToODT = function() {
          FdzProjectExportService.exportToODT($scope.fdzProject);
        };
      });
