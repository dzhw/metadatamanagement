'use strict';

angular.module('metadatamanagementApp').directive('surveyslist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/survey/' +
      'surveys.list.html.tmpl',
    controller: 'SurveysListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
