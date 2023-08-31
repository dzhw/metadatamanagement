'use strict';

angular.module('metadatamanagementApp').directive('variableSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'variable-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        variableChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'VariableSearchFilterController'
    };
  });
