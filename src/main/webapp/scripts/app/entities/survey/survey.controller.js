'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyController', function($scope, $state, Survey, ParseLinks) {

    $scope.surveys = [];
    $scope.predicate = 'id';
    $scope.reverse = true;
    $scope.page = 1;
    $scope.loadAll = function() {
      Survey.query({
        page: $scope.page - 1,
        size: 20,
        sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' :
          'desc'), 'id']
      }, function(result, headers) {
        $scope.links = ParseLinks.parse(headers('link'));
        $scope.totalItems = headers('X-Total-Count');
        $scope.surveys = result;
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
      $scope.survey = {
        title: null,
        fieldPeriod: null,
        fdzProjectName: null,
        id: null
      };
    };
  });
