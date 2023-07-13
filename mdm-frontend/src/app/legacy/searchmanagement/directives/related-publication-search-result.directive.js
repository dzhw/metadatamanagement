'use strict';

angular.module('metadatamanagementApp')
  .directive('relatedPublicationSearchResult', function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'related-publication-search-result.html.tmpl',
      scope: {
        searchQuery: '<',
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '=',
        setParams: '&'
      },
      controller: ['$scope', 'HighlightService', '$timeout',
        '$element',
        function($scope, HighlightService, $timeout,
         $element) {
        if ($scope.searchQuery) {
          $timeout(function() {
            HighlightService.apply($element[0], $scope.searchQuery);
          });
        }
      }]
    };
  });
