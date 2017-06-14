'use strict';

angular.module('metadatamanagementApp').directive('studySearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'study-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        studyChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'StudySearchFilterController'
    };
  });
