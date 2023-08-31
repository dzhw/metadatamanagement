'use strict';

angular.module('metadatamanagementApp').directive('instrumentSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'instrument-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        instrumentChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'InstrumentSearchFilterController'
    };
  });
