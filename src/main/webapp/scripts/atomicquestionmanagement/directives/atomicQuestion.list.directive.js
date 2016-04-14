'use strict';

angular.module('metadatamanagementApp').directive('atomicquestionlist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/atomicquestionmanagement/directives/' +
      'atomicQuestion.list.html.tmpl',
    controller: 'AtomicQuestionListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
