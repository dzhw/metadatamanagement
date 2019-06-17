'use strict';

angular.module('metadatamanagementApp').directive(
  'conceptSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'concept-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        conceptChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'ConceptSearchFilterController'
    };
  });
