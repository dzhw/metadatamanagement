/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('DataSetSearchDialogService',
    function($mdDialog) {
      var findDataSets = function(methodName, methodParams, count) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'DataSetSearchDialogController',
          controllerAs: 'DataSetSearchDialogController',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            methodName: methodName,
            methodParams: methodParams,
            count: count
          },
          templateUrl: 'scripts/datasetmanagement/views/' +
            'dataSetSearchDialog.html.tmpl',
        });
      };
      return {
        findDataSets: findDataSets
      };
    });
