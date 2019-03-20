'use strict';

angular.module('metadatamanagementApp').directive('previousSearchResult',
  function(SearchResultNavigatorService, SearchTypeToDetailsStateMapper,
           $state) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'previous-search-result.html.tmpl',
      link: function(scope) {
        SearchResultNavigatorService.getPreviousSearchResult().promise
          .then(function(data) {
            if (data.hits.hits.length === 1) {
              scope.previousSearchResult = data.hits.hits[0]._source;
              scope.previousSearchResultIndex = SearchResultNavigatorService
                .getPreviousSearchResultIndex();

              var state = SearchTypeToDetailsStateMapper
                .getDetailStateUrl(data.hits.hits[0]._type);
              var id = scope.previousSearchResult.masterId ?
                scope.previousSearchResult.masterId : scope
                  .previousSearchResult.id;

              scope.goToPreviousSearchResult = function() {
                $state.go(state, {
                  id: id, 'search-result-index': scope.previousSearchResultIndex
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
