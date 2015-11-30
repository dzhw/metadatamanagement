/* globals $ */
'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableController', function($rootScope, $scope, $stateParams,
        Variable, $location, ElasticSearchClient,
        VariableSearchQuerybuilder) {
      $scope.allButtonsDisabled = true;
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
              if ($scope.searchResult.length === 0) {
                $scope.IsNextEnabled = false;
              }else {
                $scope.IsNextEnabled = true;
              }
              $scope.$apply();
            }, function(error) {
              console.trace(error.message);
            });
      };
      $scope.delete = function(id) {
        Variable.get({id: id}, function(result) {
          $scope.variable = result;
          $('#deleteVariableConfirmation').modal('show');
        });
      };
      $scope.confirmDelete = function(id) {
        Variable.delete({id: id},
              function() {
                $scope.search();
                $('#deleteVariableConfirmation').modal('hide');
                $scope.clear();
              });
      };
    });
