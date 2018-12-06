'use strict';
angular.module('metadatamanagementApp').directive('instituteSearchFilter',
  function() {
    return {
      templateUrl: 'scripts/searchmanagement/directives/' +
        'institute-search-filter.html.tmpl',
      controller: 'InstituteSearchFilterController',
      restrict: 'E',
      transclude: true,
      scope: {
        currentSearchParams: '=',
        currentLanguage: '=',
        bowser: '=',
        institutionChangedCallback: '&'
      }
    };
  });
