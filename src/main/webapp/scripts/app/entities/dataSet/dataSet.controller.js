'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetController', function($scope, $state, DataSetsCollection,
     UploadService) {

    $scope.surveys = [];
    $scope.page = 1;
    $scope.loadAll = function() {
      DataSetsCollection.query({page: $scope.page - 1},
        function(result) {
        $scope.totalItems = result.page.totalElements;
        $scope.dataSets = result._embedded.dataSets;
      });
    };

    $scope.uploadTexTemplate = function(file) {
      UploadService.uploadTexTemplate(file,$scope.dataAcquisitionProject.id);
    };

    $scope.loadAll();
  });
