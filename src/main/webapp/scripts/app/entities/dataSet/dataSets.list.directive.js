'use strict';

angular.module('metadatamanagementApp').directive('datasetslist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/dataSet/' +
      'dataSets.list.html.tmpl',
    controller: 'DataSetsListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
