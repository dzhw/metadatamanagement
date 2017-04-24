'use strict';

angular.module('metadatamanagementApp')
  .directive('relatedPublicationSearchResult', function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'related-publication-search-result.html.tmpl',
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '='
      }
    };
  });
