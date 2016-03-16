'use strict';

angular.module('metadatamanagementApp').directive('atomicquestionslist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/atomicQuestion/' +
      'atomicQuestion.list.html.tmpl',
    controller: 'AtomicQuetionListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
