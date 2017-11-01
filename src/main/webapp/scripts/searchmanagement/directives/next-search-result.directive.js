'use strict';

angular.module('metadatamanagementApp').directive('nextSearchResult',
  function(SearchResultNavigatorService) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'scripts/searchmanagement/directives/' +
        'next-search-result.html.tmpl',
      link: function(scope) {
        console.log(SearchResultNavigatorService
          .getNextSearchResult());
        scope.nextSearchResult = SearchResultNavigatorService
          .getNextSearchResult();
      },
      scope: {
        nextSearchResultIndex: '=',
        currentLanguage: '=',
        bowser: '='
      }
    };
  });
