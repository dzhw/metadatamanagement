/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('DataSetSearchDialogService',
    function($mdDialog) {
      var findDataSets = function(paramObject) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'DataSetSearchDialogController',
          controllerAs: 'ctrl',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            paramObject: paramObject
          },
          templateUrl: 'scripts/datasetmanagement/views/' +
            'dataSetSearchDialog.html.tmpl',
        });
      };
      return {
        findDataSets: findDataSets
      };
    });
