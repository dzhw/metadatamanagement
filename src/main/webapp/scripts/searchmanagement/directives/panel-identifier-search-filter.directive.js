'use strict';

angular.module('metadatamanagementApp').directive('panelIdentifierSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'panel-identifier-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        panelIdentifierChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'PanelIdentifierSearchFilterController'
    };
  });
