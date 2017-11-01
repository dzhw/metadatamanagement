'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchResultNavigatorService',
  function(SearchDao, $q, ToolbarHeaderService) {
    var lastSearchParams = {};
    var lastProjectId;
    var lastPageObject;
    var lastElasticSearchType;
    var currentSearchResultIndex;
    var previousSearchResult = $q.resolve();
    var nextSearchResult = $q.resolve();

    function setCurrentSearchParams(searchParams,
      projectId, elasticSearchType, pageObject) {
      lastSearchParams = searchParams;
      lastProjectId = projectId;
      lastElasticSearchType = elasticSearchType;
      lastPageObject = pageObject;
    }

    function registerCurrentSearchResult(searchResultIndex) {
      nextSearchResult = $q.defer();
      previousSearchResult = $q.defer();
      if (searchResultIndex != null && lastPageObject) {
        currentSearchResultIndex = parseInt(searchResultIndex);
        var currentPage = Math.floor((currentSearchResultIndex - 1) /
          lastPageObject.size) + 1;
        ToolbarHeaderService.setCurrentSearchPage(currentPage);
        if (currentSearchResultIndex < lastPageObject.totalHits) {
          SearchDao.search(lastSearchParams.query,
            currentSearchResultIndex + 1,
            lastProjectId, lastSearchParams.filter,
            lastElasticSearchType,
            1, lastSearchParams.sortBy).then(function(data) {
              nextSearchResult.resolve(data);
            }).catch(function(error) {
              nextSearchResult.resolve(error);
            });
        }
        if (currentSearchResultIndex > 1) {
          SearchDao.search(lastSearchParams.query,
            currentSearchResultIndex - 1,
            lastProjectId, lastSearchParams.filter,
            lastElasticSearchType,
            1, lastSearchParams.sortBy).then(function(data) {
              previousSearchResult.resolve(data);
            }).catch(function(error) {
              previousSearchResult.resolve(error);
            });
        }
      }
    }

    function getPreviousSearchResult() {
      return previousSearchResult;
    }

    function getPreviousSearchResultIndex() {
      return currentSearchResultIndex - 1;
    }

    function getNextSearchResult() {
      return nextSearchResult;
    }

    function getNextSearchResultIndex() {
      return currentSearchResultIndex + 1;
    }

    return {
      setCurrentSearchParams: setCurrentSearchParams,
      registerCurrentSearchResult: registerCurrentSearchResult,
      getPreviousSearchResult: getPreviousSearchResult,
      getPreviousSearchResultIndex: getPreviousSearchResultIndex,
      getNextSearchResult: getNextSearchResult,
      getNextSearchResultIndex: getNextSearchResultIndex
    };
  }
);
