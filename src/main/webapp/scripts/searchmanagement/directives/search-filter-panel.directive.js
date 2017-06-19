'use strict';

angular.module('metadatamanagementApp').directive('searchFilterPanel',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'search-filter-panel.html.tmpl',
      transclude: true,
      scope: {
        currentSearchParams: '=',
        filterChangedCallback: '=',
        currentLanguage: '=',
        currentElasticsearchType: '=',
        bowser: '='
      },
      controller: 'SearchFilterPanelController'
    };
  });
