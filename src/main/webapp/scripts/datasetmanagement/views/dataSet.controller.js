'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetController', function($scope, $state,
    DataSetsCollectionResource,
    DataSetReportService, JobLoggingService) {

    $scope.dataSets = [];
    $scope.job = JobLoggingService.init();
    $scope.page = 1;
    $scope.loadAll = function() {
      DataSetsCollectionResource.query({
          page: $scope.page - 1
        },
        function(result) {
          $scope.totalItems = result.page.totalElements;
          $scope.dataSets = result._embedded.dataSets;
        });
    };

    $scope.uploadTexTemplate = function(file, dataSetId) {
      JobLoggingService.start('zip');
      DataSetReportService.uploadTexTemplate(file, dataSetId);
    };

    $scope.loadAll();
  });
