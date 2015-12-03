/* globals $ */
'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableController', function($rootScope, $scope,
        Variable, $location, $filter, ElasticSearchClient,
        VariableSearchQuerybuilder) {
      $scope.allButtonsDisabled = false;
      $scope.initSearch = function() {
        if (typeof($location.search().page) !== 'undefined') {
          $scope.page = parseInt($location.search().page);
        }else {
          $scope.page = 0;
        }
        $scope.query = $location.search().query;
        $scope.search($scope.page);
      };
      $scope.search = function(pageNumber) {
        if (pageNumber > 0) {
          $scope.page = pageNumber;
          $scope.IsPreviousEnabled = true;
        }else {
          $scope.page = 0;
          $scope.IsPreviousEnabled = false;
        }
        $location.search('query', $scope.query);
        $location.search('page', $scope.page);
        ElasticSearchClient.search
        (VariableSearchQuerybuilder.Query($scope.query, $scope.page))
            .then(function(data) {
              $scope.searchResult = data.hits.hits;
              $scope.totalHits = data.hits.total;
              if (($scope.totalHits - $scope.page - 10) > 0) {
                $scope.IsNextEnabled = true;
              }else {
                $scope.IsNextEnabled = false;
              }
              $scope.$apply();
            }, function(error) {
              console.trace(error.message);
            });
      };
      $scope.delete = function(id) {
        Variable.get({id: id}, function(result) {
          $scope.variable = result;
          var item = $filter('filter')($scope.searchResult, function(variable) {
            return variable._id === id;})[0];
          $scope.itemtoBeRemoved = $scope.searchResult.indexOf(item);
          $('#deleteVariableConfirmation').modal('show');
        });
      };
      $scope.confirmDelete = function(id) {
        Variable.delete({id: id},
              function() {
                $scope.searchResult.splice($scope.itemtoBeRemoved, 1);
                $('#deleteVariableConfirmation').modal('hide');
              });
      };
    });
