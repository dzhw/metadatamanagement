'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetController', function($scope, $state, DataSetsCollection,
    UploadService) {

    $scope.dataSets = [];
    $scope.uploadState = UploadService.getUploadState;
    $scope.page = 1;
    $scope.loadAll = function() {
      DataSetsCollection.query({
          page: $scope.page - 1
        },
        function(result) {
          $scope.totalItems = result.page.totalElements;
          $scope.dataSets = result._embedded.dataSets;
        });
    };

    $scope.uploadTexTemplate = function(file, dataSetId) {
      UploadService.uploadTexTemplate(file, dataSetId);
    };

    $scope.loadAll();
  });
