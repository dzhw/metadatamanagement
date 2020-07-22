/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service(
    'DataPackageCitationDialogService',
    function($mdDialog) {
      var showDialog = function(citationHint, event) {
        $mdDialog.show({
          templateUrl: 'scripts/ordermanagement/views/' +
          'data-package-citation-dialog.html.tmpl',
          controller: 'DataPackageCitationDialogController',
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
