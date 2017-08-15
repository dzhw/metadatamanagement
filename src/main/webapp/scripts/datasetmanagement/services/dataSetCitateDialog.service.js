/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('DataSetCitateDialogService',
    function($mdDialog) {
      var showDialog = function() {
        $mdDialog.show({
          templateUrl: 'scripts/datasetmanagement/directives/' +
          'data-set-citate-dialog.html.tmpl',
          controller: 'DataSetCitateDialogController',
          controllerAs: 'ctrl',
          clickOutsideToClose: true
        });
      };
      return {
        showDialog: showDialog
      };
    });
