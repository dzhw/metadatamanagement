'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchResultNavigatorService',
  function(SearchDao, $q, ToolbarHeaderService, $location, $transitions) {
    var searchIndex = null;
    var lastSearchParams = {};
    var lastProjectId;
    var lastPageObject;
    var lastElasticSearchType;
    var currentSearchResultIndex;
    var previousSearchResult = $q.resolve();
    var nextSearchResult = $q.resolve();
    var previousLocation = $location.absUrl();

    $transitions.onBefore({}, function(transition) {
      var toState       = transition.to();
      var toStateName   = toState.name;
      var toStateParams = transition.params();
      var toLocation = transition.router.stateService.href(
        toStateName, toStateParams);
      var fromState       = transition.from();
      var fromStateName   = fromState.name;
      var fromStateParams = transition.params('from');
      var fromLocation = transition.router.stateService.href(
        fromStateName, fromStateParams);
      if (toLocation === previousLocation &&
        fromStateName === 'search' && toStateName !== 'search') {
        lastPageObject = null;
      }
      if (!(fromStateName === 'search' && toStateName === 'search')) {
        previousLocation = fromLocation;
      }
    });

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
        if (currentSearchResultIndex < lastPageObject.totalHits &&
          currentSearchResultIndex < 10000) {
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
      } else {
        if (searchResultIndex != null) {
          searchResultIndex = null;
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
  }
);
