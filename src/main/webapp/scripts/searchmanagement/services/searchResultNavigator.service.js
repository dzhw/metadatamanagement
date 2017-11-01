'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchResultNavigatorService',
  function(SearchDao) {
    var lastSearchParams = {};
    var lastProjectId;
    var lastPageObject;
    var lastElasticSearchType;
    var previousSearchResult;
    var nextSearchResult;

    function setCurrentSearchParams(searchParams,
      projectId, elasticSearchType, pageObject) {
      lastSearchParams = searchParams;
      lastProjectId = projectId;
      lastElasticSearchType = elasticSearchType;
      lastPageObject = pageObject;
    }

    function registerCurrentSearchResult(searchResultIndex) {
      if (searchResultIndex != null && lastPageObject) {
        searchResultIndex = parseInt(searchResultIndex);
        SearchDao.search(lastSearchParams.query,
          searchResultIndex + 1,
          lastProjectId, lastSearchParams.filter,
          lastElasticSearchType,
          1, lastSearchParams.sortBy).then(function(data) {
            if (data.hits.hits.length === 1) {
              nextSearchResult = data.hits.hits[0]._source;
            }
          });
        if (searchResultIndex > 1) {
          SearchDao.search(lastSearchParams.query,
            searchResultIndex - 1,
            lastProjectId, lastSearchParams.filter,
            lastElasticSearchType,
            1, lastSearchParams.sortBy).then(function(data) {
              if (data.hits.hits.length === 1) {
                previousSearchResult = data.hits.hits[0]._source;
              }
            });
        }
      }
    }

    function getPreviousSearchResult() {
      return previousSearchResult;
    }

    function getNextSearchResult() {
      return nextSearchResult;
    }

    return {
      setCurrentSearchParams: setCurrentSearchParams,
      registerCurrentSearchResult: registerCurrentSearchResult,
      getPreviousSearchResult: getPreviousSearchResult,
      getNextSearchResult: getNextSearchResult
    };
  }
);
