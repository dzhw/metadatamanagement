'use strict';

angular.module('metadatamanagementApp').directive('sponsorSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'sponsor-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        sponsorChangedCallback: '&',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'SponsorSearchFilterController'
    };
  });
