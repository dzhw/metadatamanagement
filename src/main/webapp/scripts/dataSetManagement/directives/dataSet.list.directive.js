'use strict';

angular.module('metadatamanagementApp').directive('datasetlist',
function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/dataSetManagement/directives/' +
      'dataSet.list.html.tmpl',
    controller: 'DataSetListController',
    scope: {
      params: '=',
      currentLanguage: '=',
      currentPage: '='
    }
  };
});
