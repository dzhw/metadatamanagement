'use strict';

angular.module('metadatamanagementApp').directive('datasetSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-set-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        datasetChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'DataSetSearchFilterController'
    };
  });
