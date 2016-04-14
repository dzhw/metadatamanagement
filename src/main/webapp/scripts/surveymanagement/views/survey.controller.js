'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyController', function($scope, $state, SurveyCollection) {

    $scope.surveys = [];
    $scope.page = 1;
    $scope.loadAll = function() {
      SurveyCollection.query({page: $scope.page - 1},
        function(result) {
        $scope.totalItems = result.page.totalElements;
        $scope.surveys = result._embedded.surveys;
      });
    };

    $scope.loadAll();
  });
