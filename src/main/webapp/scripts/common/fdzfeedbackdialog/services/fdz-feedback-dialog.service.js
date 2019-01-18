'use strict';

angular.module('metadatamanagementApp').service('FdzFeedbackDialogService',
    function($mdDialog, localStorageService, $rootScope) {
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

      var deleteOption = function() {
        //delete unused option from all browsers...
        localStorageService.remove();
      };

      return {
        showDialog: showDialog,
        deleteOption: deleteOption
      };
    });
