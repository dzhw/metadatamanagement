'use strict';

angular.module('metadatamanagementApp').directive('previousSearchResult',
  function(SearchResultNavigatorService, SearchTypeToDetailsStateMapper,
     LanguageService, $mdSidenav) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'previous-search-result.html.tmpl',
      link: function(scope) {
        scope.leftMarginStyle = $mdSidenav('SideNavBar').isLockedOpen() ?
          {left: 320} : {left: 0} ;
        SearchResultNavigatorService.getPreviousSearchResult().promise.
          then(function(data) {
            if (data.hits.hits.length === 1) {
              scope.previousSearchResult = data.hits.hits[0]._source;
              scope.previousSearchResultIndex = SearchResultNavigatorService
                .getPreviousSearchResultIndex();
              scope.url = SearchTypeToDetailsStateMapper
                .getDetailStateUrl(data.hits.hits[0]._type,
                {
                  lang: LanguageService.getCurrentInstantly(),
                  id: scope.previousSearchResult.id,
                  'search-result-index': scope.previousSearchResultIndex
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
