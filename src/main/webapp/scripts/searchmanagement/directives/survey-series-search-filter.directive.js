'use strict';

angular.module('metadatamanagementApp').directive('surveySeriesSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'survey-series-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        surveySeriesChangedCallback: '=',
        currentLanguage: '=',
        selectedFilters: '=',
        bowser: '='
      },
      controller: 'SurveySeriesSearchFilterController'
    };
  });
