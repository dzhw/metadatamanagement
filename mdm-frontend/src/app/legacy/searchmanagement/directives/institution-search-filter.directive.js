'use strict';
angular.module('metadatamanagementApp').directive('institutionSearchFilter',
  function() {
    return {
      templateUrl: 'scripts/searchmanagement/directives/' +
        'institution-search-filter.html.tmpl',
      controller: 'InstitutionSearchFilterController',
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
