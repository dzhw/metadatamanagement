'use strict';

angular.module('metadatamanagementApp').service('FdzWelcomeDialogService',
    function($mdDialog, localStorageService, $rootScope) {
        var showDialog = function(openByNavbarFeedbackButton) {
          console.log(openByNavbarFeedbackButton);
          if (!localStorageService.get('closeWelcomeDialogForever') ||
            openByNavbarFeedbackButton) {
            $mdDialog.show({
              templateUrl: 'scripts/common/fdzwelcomedialog/directives/' +
              'fdz-welcome-dialog.html.tmpl',
              controller: 'FdzWelcomeDialogController',
              locals: {
                bowser: $rootScope.bowser
              },
              clickOutsideToClose: true,
              closeTo: '#feedBackButton'
            });
          }
        };
        return {
          showDialog: showDialog
        };
      });
