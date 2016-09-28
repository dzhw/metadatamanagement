/* global document*/

'use strict';
angular.module('metadatamanagementApp')
.service('DataSetSearchDialogService',
  function($mdDialog, blockUI, DataSetSearchResource) {
    var dataSets = [];
    var showDialog = function() {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'DataSetSearchDialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          dataSets: dataSets
        },
        templateUrl: 'scripts/datasetmanagement/directives/' +
        'dataSetSearchDialog.html.tmpl',
      });
    };
    var findByVariableId = function(variableId) {
      blockUI.start();
      console.log('sds');
      DataSetSearchResource.findByVariableId(variableId)
      .then(function(items) {
        console.log(items);
        dataSets = items.hits.hits;
        blockUI.stop();
        showDialog();
      });
    };
    return {
      findByVariableId: findByVariableId
    };
  });
