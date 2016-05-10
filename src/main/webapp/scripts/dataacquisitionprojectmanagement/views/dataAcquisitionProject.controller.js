'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectController',
    function($scope, $state, DataAcquisitionProjectCollectionResource) {
      $scope.dataAcquisitionProjects = [];
      $scope.page = 1;
      $scope.loadAll = function() {
        DataAcquisitionProjectCollectionResource.query({
            page: $scope.page - 1
          },
          function(result) {
            $scope.totalItems = result.page.totalElements;
            $scope.dataAcquisitionProjects =
              result._embedded.dataAcquisitionProjects;
          });
      };
      $scope.loadAll();
    });
