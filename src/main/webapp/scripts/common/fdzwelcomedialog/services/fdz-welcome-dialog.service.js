'use strict';

angular.module('metadatamanagementApp').service('FdzWelcomeDialogService',
    function($mdDialog, localStorageService, $rootScope) {
      var showDialog = function() {
        $mdDialog.show({
          templateUrl: 'scripts/common/fdzwelcomedialog/directives/' +
          'fdz-welcome-dialog.html.tmpl',
          controller: 'FdzWelcomeDialogController',
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
        localStorageService.remove('closeWelcomeDialogForever');
      };

      return {
        showDialog: showDialog,
        deleteOption: deleteOption
      };
    });
