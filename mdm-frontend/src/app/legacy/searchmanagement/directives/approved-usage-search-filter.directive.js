'use strict';

angular.module('metadatamanagementApp').directive('approvedUsageSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'approved-usage-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        approvedUsageChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'ApprovedUsageSearchFilterController'
    };
  });
