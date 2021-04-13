/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service(
    'DataPackageCitationDialogService',
    function($mdDialog) {
      var showDialog = function(citationHint, methodReportsCitationHint,
        event) {
        $mdDialog.show({
          templateUrl: 'scripts/ordermanagement/views/' +
          'data-package-citation-dialog.html.tmpl',
          controller: 'DataPackageCitationDialogController',
          controllerAs: 'ctrl',
          targetEvent: event,
          locals: {
            dataPackageCitationHint: citationHint,
            methodReportsCitationHint: methodReportsCitationHint
          },
          clickOutsideToClose: true,
          fullscreen: true
        });
      };
      return {
        showDialog: showDialog
      };
    });
