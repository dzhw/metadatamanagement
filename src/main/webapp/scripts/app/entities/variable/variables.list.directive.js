'use strict';

angular.module('metadatamanagementApp').directive('variableslist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/variable/' +
      'variables.list.html.tmpl',
    controller: 'VariablesListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
