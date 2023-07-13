'use strict';

angular.module('metadatamanagementApp').directive('variableSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'variable-search-result.html.tmpl',
      scope: {
        searchQuery: '<',
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '=',
        setParams: '&'
      },
      controller: ['$scope', 'Principal', 'HighlightService', '$timeout',
        '$element',
        function($scope, Principal, HighlightService, $timeout,
         $element) {
        if ($scope.searchQuery) {
          $timeout(function() {
            HighlightService.apply($element[0], $scope.searchQuery);
          });
        }
        $scope.isLoggedIn = Principal.loginName();
      }]
    };
  });
