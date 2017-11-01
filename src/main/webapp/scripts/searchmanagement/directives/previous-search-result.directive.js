'use strict';

angular.module('metadatamanagementApp').directive('previousSearchResult',
  function(SearchResultNavigatorService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'previous-search-result.html.tmpl',
      link: function(scope) {
        scope.previousSearchResultLoaded = false;
        SearchResultNavigatorService.getPreviousSearchResult().promise.
          then(function(data) {
            if (data.hits.hits.length === 1) {
              scope.previousSearchResultLoaded = true;
              scope.previousSearchResult = data.hits.hits[0]._source;
              scope.previousSearchResultIndex = SearchResultNavigatorService
                .getPreviousSearchResultIndex();
            }
          });
      },
      scope: {
        currentLanguage: '=',
        bowser: '='
      }
    };
  });
