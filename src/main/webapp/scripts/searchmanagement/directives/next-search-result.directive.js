'use strict';

angular.module('metadatamanagementApp').directive('nextSearchResult',
  function(SearchResultNavigatorService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'next-search-result.html.tmpl',
      link: function(scope) {
        scope.nextSearchResultLoaded = false;
        SearchResultNavigatorService.getNextSearchResult().promise.
          then(function(data) {
            if (data.hits.hits.length === 1) {
              scope.nextSearchResultLoaded = true;
              scope.nextSearchResult = data.hits.hits[0]._source;
              scope.nextSearchResultIndex = SearchResultNavigatorService
                .getNextSearchResultIndex();
            }
          });
      },
      scope: {
        currentLanguage: '=',
        bowser: '='
      }
    };
  });
