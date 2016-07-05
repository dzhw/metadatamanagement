'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetController', function($scope, $state,
    DataSetCollectionResource,
    DataSetReportService, JobLoggingService) {

    $scope.dataSets = [];
    $scope.job = JobLoggingService.getCurrentJob();
    $scope.page = 1;
    $scope.loadAll = function() {
      DataSetCollectionResource.query({
          page: $scope.page - 1
        },
        function(result) {
          $scope.totalItems = result.page.totalElements;
          $scope.dataSets = result._embedded.dataSets;
        });
    };

    $scope.uploadTexTemplate = function(file, dataSetId) {
      DataSetReportService.uploadTexTemplate(file, dataSetId);
    };

    $scope.loadAll();
  });
