'use strict';

angular.module('metadatamanagementApp').directive('accessWaySearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'access-way-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        accessWayChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'AccessWaySearchFilterController'
    };
  });
