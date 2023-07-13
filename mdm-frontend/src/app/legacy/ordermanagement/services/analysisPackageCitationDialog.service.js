/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('AnalysisPackageCitationDialogService', ['$mdDialog', 
    function($mdDialog) {
      var showDialog = function(analysisPackage,
        event) {
        $mdDialog.show({
          templateUrl: 'scripts/ordermanagement/views/' +
          'analysis-package-citation-dialog.html.tmpl',
          controller: 'AnalysisPackageCitationDialogController',
          controllerAs: 'ctrl',
          targetEvent: event,
          locals: {
            analysisPackage: analysisPackage
          },
          clickOutsideToClose: true,
          fullscreen: true
        });
      };
      return {
        showDialog: showDialog
      };
    }]);

