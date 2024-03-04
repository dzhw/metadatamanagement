'use strict';

angular.module('metadatamanagementApp').directive('approvedUsageListSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'approved-usage-list-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        approvedUsageListChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'ApprovedUsageListSearchFilterController'
    };
  });
