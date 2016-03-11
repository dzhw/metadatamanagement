'use strict';

angular.module('metadatamanagementApp').directive('variablelist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/variable/' +
      'variable.list.html.tmpl',
    controller: 'VariableListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
