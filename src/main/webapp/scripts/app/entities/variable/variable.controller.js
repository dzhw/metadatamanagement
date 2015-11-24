/* globals $ */
'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableController', function($scope, Variable, ParseLinks,
        $rootScope, $location, BookmarkableUrl, ElasticSearchClient,
        VariableSearchQuerybuilder) {
      $scope.isDisabled = 'true';
      BookmarkableUrl.setUrlLanguage($location, $rootScope);
      $scope.$on('$locationChangeSuccess', function() {
        $scope.query = $location.search().query;
      });
      $scope.initsearch = function() {
        if ($location.search().query) {
          $scope.query = $location.search().query;
        }
        $scope.search();
      };
      $scope.search = function() {
        $location.search('query', $scope.query);
        ElasticSearchClient.search
        (VariableSearchQuerybuilder.Query($scope.query))
            .then(function(data) {
              $scope.searchResult = data.hits.hits;
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

      $scope.refresh = function() {
        $scope.clear();
        $scope.search();
      };

      $scope.clear = function() {
        $scope.variable = {
          name: null,
          dataType: null,
          scaleLevel: null,
          label: null,
          id: null
        };
      };
    });
