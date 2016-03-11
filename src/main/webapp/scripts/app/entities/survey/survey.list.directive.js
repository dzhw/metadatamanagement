'use strict';

angular.module('metadatamanagementApp').directive('surveylist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/survey/' +
      'survey.list.html.tmpl',
    controller: 'SurveyListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
