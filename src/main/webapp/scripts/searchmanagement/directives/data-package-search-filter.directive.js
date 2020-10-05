'use strict';

angular.module('metadatamanagementApp').directive('datapackageSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-package-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        datapackageChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'DataPackageSearchFilterController'
    };
  });
