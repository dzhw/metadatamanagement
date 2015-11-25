/* globals $ */
'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableController', function($scope, Variable, ParseLinks,
        $rootScope, $location, BookmarkableUrl, ElasticSearchClient,
        VariableSearchQuerybuilder) {
      $scope.isDisabled = 'true';
      BookmarkableUrl.setUrlLanguage($location, $rootScope);
      /*$scope.$on('$locationChangeSuccess', function() {
        $scope.refresh();
        $scope.query = $location.search().query;
        $scope.page = $location.search().page;
      });*/
      $scope.initsearch = function() {
        $scope.page = 0;
        if ($location.search().query) {
          $scope.query = $location.search().query;
        }
        if ($location.search().page) {
          $scope.page = parseInt($location.search().page);
        }
        $scope.loadPage($scope.page);
      };
      $scope.loadPage = function(pagenumber) {
        if (pagenumber > 0) {
          $scope.IsPreviousStateEnable = '';
          $scope.page = pagenumber;
          $scope.IsPreviousClickEnable = true;
        }else {
          $scope.IsPreviousStateEnable = 'disabled';
          $scope.IsPreviousClickEnable = false;
          $scope.page = 0;
        }
        $scope.search();
      };
      $scope.search = function() {
        $location.search('query', $scope.query);
        $location.search('page', $scope.page);
        ElasticSearchClient.search
        (VariableSearchQuerybuilder.Query($scope.query, $scope.page))
            .then(function(data) {
              $scope.searchResult = data.hits.hits;
              if ($scope.searchResult.length === 0) {
                $scope.IsNextStateEnable = 'disabled';
                $scope.IsNextClickEnable = false;
              }else {
                $scope.IsNextStateEnable = '';
                $scope.IsNextClickEnable = true;
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

      $scope.refresh = function() {
        $scope.clear();
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
