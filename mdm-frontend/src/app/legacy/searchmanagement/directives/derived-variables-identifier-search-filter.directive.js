'use strict';

angular.module('metadatamanagementApp')
  .directive('derivedVariablesIdentifierSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'derived-variables-identifier-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        derivedVariablesIdentifierChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'DerivedVariablesIdentifierSearchFilterController'
    };
  });
