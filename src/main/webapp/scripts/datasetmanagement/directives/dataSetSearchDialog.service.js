/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('DataSetSearchDialogService',
    function($mdDialog, blockUI, DataSetSearchResource, CleanJSObjectService) {
      var dataSets = [];
      var showDialog = function() {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'DataSetSearchDialogController',
          controllerAs: 'dataSetSearchDialogController',
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
        DataSetSearchResource.findByVariableId(variableId)
          .then(function(items) {
            if (!CleanJSObjectService.isNullOrEmpty(items)) {
              dataSets = items.hits.hits;
              blockUI.stop();
              showDialog();
            } else {
              blockUI.stop();
            }
          });
      };
      return {
        findByVariableId: findByVariableId
      };
    });
