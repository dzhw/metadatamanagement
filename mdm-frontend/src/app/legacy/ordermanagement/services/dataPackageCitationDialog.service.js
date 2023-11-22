/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('DataPackageCitationDialogService', ['$mdDialog', 
    function($mdDialog) {
      var showDialog = function(accessWay, dataPackage,
        event) {
        $mdDialog.show({
          templateUrl: 'scripts/ordermanagement/views/' +
          'data-package-citation-dialog.html.tmpl',
          controller: 'DataPackageCitationDialogController',
          controllerAs: 'ctrl',
          targetEvent: event,
          locals: {
            accessWay: accessWay,
            dataPackage: dataPackage
          },
          clickOutsideToClose: true,
          fullscreen: true
        });
      };
      return {
        showDialog: showDialog
      };
    }]);

