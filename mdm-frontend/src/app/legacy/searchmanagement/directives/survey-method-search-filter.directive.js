'use strict';
angular.module('metadatamanagementApp').directive('surveyMethodSearchFilter',
  function() {
    return {
      templateUrl: 'scripts/searchmanagement/directives/' +
        'survey-method-search-filter.html.tmpl',
      controller: 'SurveyMethodSearchFilterController',
      restrict: 'E',
      transclude: true,
      scope: {
        currentSearchParams: '=',
        currentLanguage: '=',
        bowser: '=',
        surveyMethodChangedCallback: '&'
      }
    };
  });
