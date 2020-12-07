'use strict';

angular.module('metadatamanagementApp').directive(
  'repeatedMeasurementIdentifierSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'repeated-measurement-identifier-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        repeatedMeasurementIdentifierChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'RepeatedMeasurementIdentifierSearchFilterController'
    };
  });
