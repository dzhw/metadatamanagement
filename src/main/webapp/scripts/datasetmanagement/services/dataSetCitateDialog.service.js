/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('DataSetCitateDialogService',
    function($mdDialog) {
      var showDialog = function(citationHint, event) {
        $mdDialog.show({
          templateUrl: 'scripts/datasetmanagement/views/' +
          'data-set-citate-dialog.html.tmpl',
          controller: 'DataSetCitateDialogController',
          controllerAs: 'ctrl',
          targetEvent: event,
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
