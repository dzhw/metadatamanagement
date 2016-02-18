'use strict';

angular.module('metadatamanagementApp')
    .controller('DataAcquisitionProjectController',
    function($scope, $state, DataAcquisitionProjectCollection) {
      $scope.dataAcquisitionProjects = [];
      $scope.page = 1;
      $scope.loadAll = function() {
        DataAcquisitionProjectCollection.query({page: $scope.page - 1},
          function(result) {
          $scope.totalItems = result.page.totalElements;
          $scope.dataAcquisitionProjects = result._embedded.fdzProjects;
        });
      };
      $scope.loadAll();
    });
