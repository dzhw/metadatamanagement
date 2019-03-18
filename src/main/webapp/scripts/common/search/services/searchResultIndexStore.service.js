'use strict';

angular.module('metadatamanagementApp').service('SearchResultIndexStore',
  function() {

    var searchIndex;

    var setIndex = function(index) {
      searchIndex = index;
    };

    var getIndex = function() {
      return searchIndex;
    };

    var currentSearchResultIndex = function(index) {
      if (angular.isDefined(index)) {
        setIndex(index);
      } else {
        return getIndex();
      }
    };

    return {
      currentSearchResultIndex: currentSearchResultIndex
    };
  });
