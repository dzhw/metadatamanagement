/* globals $ */
'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableController', function($rootScope, $state, $scope,
        Variable, ParseLinks, $location, BookmarkableUrl, ElasticSearchClient,
        VariableSearchQuerybuilder) {
      $scope.isDisabled = 'true';
      BookmarkableUrl.setUrlLanguage($location, $rootScope);
      $scope.initsearch = function() {
        if (typeof($rootScope.bookmarkableUrlQueryParameter) !== 'undefined') {
          $scope.query = $rootScope.bookmarkableUrlQueryParameter;
          $rootScope.bookmarkableUrlQueryParameter = undefined;
        }
        if (typeof($rootScope.bookmarkableUrlPageParameter) !== 'undefined') {
          $scope.page = parseInt($rootScope.bookmarkableUrlPageParameter);
          $rootScope.bookmarkableUrlPageParameter = undefined;
        }else {
          $scope.page = 0;
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
        }
        $scope.search();
      };
      $scope.search = function() {
        $state.go('variable', {query: $scope.query, page: $scope.page},
            {notify: false});
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
      $state.go('variable', {query: $scope.query, page: $scope.page},
          {notify: true});
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
