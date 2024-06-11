'use strict';

angular.module('metadatamanagementApp').factory('SearchResultNavigatorService', [
  'SearchDao', 
  '$q', 
  'BreadcrumbService', 
  function(SearchDao, $q, BreadcrumbService) {
    var searchIndex = null;
    var lastSearchParams = {};
    var lastProjectId;
    var lastPageObject;
    var lastElasticSearchType;
    var currentSearchResultIndex;
    var previousSearchResult = $q.resolve();
    var nextSearchResult = $q.resolve();

    function setCurrentSearchParams(searchParams,
      projectId, elasticSearchType, pageObject) {
      if (searchParams && !searchParams.hasOwnProperty('filter')) {
        searchParams.filter = {};
      }
      lastSearchParams = searchParams;
      lastProjectId = projectId;
      lastElasticSearchType = elasticSearchType;
      lastPageObject = pageObject;
    }

    function registerCurrentSearchResult(searchResultIndex) {
      if (!angular.isDefined(searchResultIndex)) {
        searchResultIndex = searchIndex;
      }
      nextSearchResult = $q.defer();
      previousSearchResult = $q.defer();
      if (searchResultIndex != null && lastPageObject) {
        currentSearchResultIndex = parseInt(searchResultIndex);
        var currentPage = Math.floor((currentSearchResultIndex - 1) /
        lastPageObject.size) + 1;
        BreadcrumbService.setCurrentSearchPage(currentPage);
        if (currentSearchResultIndex < lastPageObject.totalHits &&
          currentSearchResultIndex < 10000) {
          SearchDao.search(lastSearchParams.query,
            currentSearchResultIndex + 1,
            lastProjectId, lastSearchParams.filter,
            lastElasticSearchType,
            1,
            null,
            null,
            lastSearchParams.filter).then(function(data) {
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
            1,
            null,
            null,
            lastSearchParams.filter).then(function(data) {
              previousSearchResult.resolve(data);
            }).catch(function(error) {
              previousSearchResult.resolve(error);
            });
        }
      } else {
        if (searchResultIndex != null) {
          searchIndex = null;
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

    function setSearchIndex(newIndex) {
      searchIndex = newIndex;
    }

    function getSearchIndex() {
      return searchIndex;
    }

    return {
      setCurrentSearchParams: setCurrentSearchParams,
      registerCurrentSearchResult: registerCurrentSearchResult,
      getPreviousSearchResult: getPreviousSearchResult,
      getPreviousSearchResultIndex: getPreviousSearchResultIndex,
      getNextSearchResult: getNextSearchResult,
      getNextSearchResultIndex: getNextSearchResultIndex,
      setSearchIndex: setSearchIndex,
      getSearchIndex: getSearchIndex
    };
  }]);

