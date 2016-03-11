'use strict';

angular.module('metadatamanagementApp').directive('datasetlist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/app/entities/dataSet/' +
      'dataSet.list.html.tmpl',
    controller: 'DataSetListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
