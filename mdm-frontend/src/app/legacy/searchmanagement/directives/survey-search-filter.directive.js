'use strict';

angular.module('metadatamanagementApp').directive('surveySearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'survey-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        surveyChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'SurveySearchFilterController'
    };
  });
