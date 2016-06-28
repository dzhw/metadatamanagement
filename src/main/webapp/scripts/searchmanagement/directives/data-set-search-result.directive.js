'use strict';

angular.module('metadatamanagementApp').directive('datasetSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-set-search-result.html.tmpl',
      scope: {
        searchResult: '='
      }
    };
  });
