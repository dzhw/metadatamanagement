/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').directive('studySeriesSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'study-series-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        studySeriesChangedCallback: '=',
        currentLanguage: '=',
        selectedFilters: '=',
        bowser: '='
      },
      controller: 'StudySeriesSearchFilterController'
    };
  });
