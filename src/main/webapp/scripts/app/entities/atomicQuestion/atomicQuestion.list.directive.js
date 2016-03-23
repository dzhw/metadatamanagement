'use strict';

angular.module('metadatamanagementApp').directive('atomicquestionslist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/atomicQuestion/' +
      'atomicQuestion.list.html.tmpl',
    controller: 'AtomicQuestionListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
