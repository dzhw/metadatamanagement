'use strict';

angular.module('metadatamanagementApp')
    .controller('FdzProjectController',
    function($scope, $state, FdzProject, ParseLinks) {
      $scope.fdzProjects = [];
      $scope.predicate = 'name';
      $scope.reverse = true;
      $scope.page = 1;
      $scope.loadAll = function() {
        FdzProject.query({page: $scope.page - 1, size: 20, sort:
          [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'name']},
          function(result, headers) {
          $scope.links = ParseLinks.parse(headers('link'));
          $scope.totalItems = headers('X-Total-Count');
          $scope.fdzProjects = result;
        });
      };
      $scope.loadPage = function(page) {
        $scope.page = page;
        $scope.loadAll();
      };
      $scope.loadAll();

      $scope.refresh = function() {
        $scope.loadAll();
        $scope.clear();
      };

      $scope.clear = function() {
        $scope.fdzProject = {
          name: null,
          sufDoi: null,
          cufDoi: null
        };
      };
    });
