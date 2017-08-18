/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('DataSetCitateDialogService',
    function($mdDialog) {
      var showDialog = function(citationHint) {
        $mdDialog.show({
          templateUrl: 'scripts/datasetmanagement/views/' +
          'data-set-citate-dialog.html.tmpl',
          controller: 'DataSetCitateDialogController',
          controllerAs: 'ctrl',
          locals: {
            citation: citationHint
          },
          clickOutsideToClose: true
        });
      };
      return {
        showDialog: showDialog
      };
    });
