'use strict';

angular.module('metadatamanagementApp').directive(
  'relatedPublicationSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'related-publication-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        relatedPublicationChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'RelatedPublicationSearchFilterController'
    };
  });
