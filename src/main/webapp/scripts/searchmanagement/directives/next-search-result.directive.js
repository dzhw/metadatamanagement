'use strict';

angular.module('metadatamanagementApp').directive('nextSearchResult',
  function(SearchResultNavigatorService, SearchTypeToDetailsStateMapper,
           $state) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'next-search-result.html.tmpl',
      link: function(scope) {
        SearchResultNavigatorService.getNextSearchResult().promise
          .then(function(data) {
            if (data.hits.hits.length === 1) {
              scope.nextSearchResult = data.hits.hits[0]._source;
              scope.nextSearchResultIndex = SearchResultNavigatorService
                .getNextSearchResultIndex();

              var state = SearchTypeToDetailsStateMapper
                .getDetailStateUrl(data.hits.hits[0]._type);
              var id = scope.nextSearchResult.masterId ?
                scope.nextSearchResult.masterId : scope.nextSearchResult.id;

              scope.goToNextSearchResult = function() {
                $state.go(state, {
                  id: id, 'search-result-index':
                  scope.nextSearchResultIndex
                });
              };
            }
          });
      },
      scope: {
        currentLanguage: '=',
        bowser: '='
      }
    };
  });
