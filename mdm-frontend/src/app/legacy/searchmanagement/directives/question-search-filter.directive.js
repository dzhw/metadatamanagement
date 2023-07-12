'use strict';

angular.module('metadatamanagementApp').directive('questionSearchFilter',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'question-search-filter.html.tmpl',
      scope: {
        currentSearchParams: '=',
        questionChangedCallback: '=',
        currentLanguage: '=',
        bowser: '='
      },
      controller: 'QuestionSearchFilterController'
    };
  });
