/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('nextSearchResult', ['SearchResultNavigatorService', 'SearchTypeToDetailsStateMapper', '$state', 'Principal', 
  function(SearchResultNavigatorService, SearchTypeToDetailsStateMapper,
           $state, Principal) {
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
                .getDetailStateUrl(data.hits.hits[0]._index);
              var id = scope.nextSearchResult.masterId ?
                scope.nextSearchResult.masterId : scope.nextSearchResult.id;

              scope.goToNextSearchResult = function() {
                $state.go(state, {
                  id: id,
                  'search-result-index': scope.nextSearchResultIndex,
                  version: Principal.loginName() ? undefined
                    : _.get(scope.nextSearchResult, 'release.version'),
                  lang: scope.currentLanguage
                }, {inherit: false});
              };
            }
          });
      },
      scope: {
        currentLanguage: '=',
        bowser: '='
      }
    };
  }]);

