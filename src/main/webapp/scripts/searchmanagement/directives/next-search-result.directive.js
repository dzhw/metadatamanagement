'use strict';

angular.module('metadatamanagementApp').directive('nextSearchResult',
  function(SearchResultNavigatorService, SearchTypeToDetailsStateMapper,
    LanguageService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'next-search-result.html.tmpl',
      link: function(scope) {
        SearchResultNavigatorService.getNextSearchResult().promise.
          then(function(data) {
            if (data.hits.hits.length === 1) {
              scope.nextSearchResult = data.hits.hits[0]._source;
              scope.nextSearchResultIndex = SearchResultNavigatorService
                .getNextSearchResultIndex();
              scope.url = SearchTypeToDetailsStateMapper
                .getDetailStateUrl(data.hits.hits[0]._type,
                {
                  lang: LanguageService.getCurrentInstantly(),
                  id: scope.nextSearchResult.id,
                  'search-result-index': scope.nextSearchResultIndex
                });
            }
          });
      },
      scope: {
        currentLanguage: '=',
        bowser: '='
      }
    };
  });
