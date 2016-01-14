'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyController', function($scope, $state, Survey) {

    $scope.surveys = [];
    $scope.page = 1;
    $scope.loadAll = function() {
      Survey.query({page: $scope.page - 1},
        function(result) {
        $scope.totalItems = result.page.totalElements;
        $scope.surveys = result._embedded.surveys;
      });
    };

    $scope.loadAll();
  });
