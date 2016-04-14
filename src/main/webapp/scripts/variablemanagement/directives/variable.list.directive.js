'use strict';

angular.module('metadatamanagementApp').directive('variablelist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/variablemanagement/directives/' +
      'variable.list.html.tmpl',
    controller: 'VariableListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
