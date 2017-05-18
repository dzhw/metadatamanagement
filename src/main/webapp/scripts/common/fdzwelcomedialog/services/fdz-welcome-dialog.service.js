'use strict';

angular.module('metadatamanagementApp').service('FdzWelcomeDialogService',
    function($mdDialog, localStorageService, $rootScope) {
        var showDialog = function() {
          if (!localStorageService.get('closeWelcomeDialogForever')) {
            $mdDialog.show({
              templateUrl: 'scripts/common/fdzwelcomedialog/directives/' +
              'fdz-welcome-dialog.html.tmpl',
              controller: 'FdzWelcomeDialogController',
              locals: {
                bowser: $rootScope.bowser
              },
              clickOutsideToClose: true,
              closeTo: angular.element('#feedBackButton')
            });
          }
        };
        return {
          showDialog: showDialog
        };
      });
