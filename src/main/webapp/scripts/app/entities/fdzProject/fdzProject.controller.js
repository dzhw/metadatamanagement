'use strict';

angular.module('metadatamanagementApp')
    .controller('FdzProjectController',
    function($scope, $state, FdzProjectCollection) {
      $scope.fdzProjects = [];
      $scope.page = 1;
      $scope.loadAll = function() {
        FdzProjectCollection.query({page: $scope.page - 1},
          function(result) {
          $scope.totalItems = result.page.totalElements;
          $scope.fdzProjects = result._embedded.fdzProjects;
        });
      };
      $scope.loadAll();
    });
