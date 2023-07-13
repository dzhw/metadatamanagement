'use strict';

angular.module('metadatamanagementApp').service('FdzFeedbackDialogService', ['$mdDialog', '$rootScope', 
    function($mdDialog, $rootScope) {
      var showDialog = function() {
        $mdDialog.show({
          templateUrl: 'scripts/common/fdzfeedbackdialog/directives/' +
          'fdz-feedback-dialog.html.tmpl',
          controller: 'FdzFeedbackDialogController',
          locals: {
            bowser: $rootScope.bowser
          },
          clickOutsideToClose: true,
          closeTo: '#feedBackButton',
          fullscreen: true
        });
      };

      return {
        showDialog: showDialog
      };
    }]);

