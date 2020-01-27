'use strict';

angular.module('metadatamanagementApp').directive('questionSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'question-search-result.html.tmpl',
      scope: {
        searchQuery: '<',
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '=',
        setParams: '&'
      },
      controller: function($scope, Principal, $timeout, $element,
        HighlightService) {
        if ($scope.searchQuery) {
          $timeout(function() {
            HighlightService.apply($element[0], $scope.searchQuery);
          });
        }
        $scope.isLoggedIn = Principal.loginName();
        $scope.setCSRParams = function() {
          $scope.setCurrentSearchParams();
        };
      }
    };
  });
