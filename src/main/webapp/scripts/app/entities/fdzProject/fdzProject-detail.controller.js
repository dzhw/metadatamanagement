'use strict';

angular.module('metadatamanagementApp')
    .controller('FdzProjectDetailController',
      function($scope, $rootScope, $stateParams,
        entity, FdzProjectExportService, ExcelParser) {
        $scope.fdzProject = entity;
        $scope.fileInputContent = '';
        $scope.files = [];
        $scope.$watch('files',function() {
          if ($scope.files.length > 0) {
            ExcelParser.readFileAsync($scope.files[0])
            .then(function(fileInputContent) {
              $scope.fileInputContent = fileInputContent;
            });
          }
        });

        $scope.exportToODT = function() {
          FdzProjectExportService.exportToODT($scope.fdzProject);
        };
      });
