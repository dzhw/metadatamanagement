/* globals document */
'use strict';
angular.module('metadatamanagementApp').service('WelcomeDialogService',
  function($mdDialog, LanguageService) {

    var createDialogController = function() {
      return function($scope, $mdDialog, locals) {
        $scope.username = locals.username;
        $scope.displayDeactivateDialogOption =
          locals.displayDeactivateDialogOption;
        $scope.language = locals.currentLanguage;
        $scope.data = {
          doNotShowAgain: false
        };

        $scope.closeDialog = function() {
          $mdDialog.hide($scope.data.doNotShowAgain);
        };
      };
    };

    var display = function(username, displayDeactivateDialogOption) {
      displayDeactivateDialogOption = angular
        .isDefined(displayDeactivateDialogOption) ?
        displayDeactivateDialogOption : true;

      return $mdDialog.show({
        controller: createDialogController(),
        templateUrl: 'scripts/usermanagement/views/welcome-dialog.html.tmpl',
        parent: angular.element(document.body),
        closeTo: '#fdz-welcome-dialog-item',
        clickOutsideToClose: false,
        fullscreen: true,
        locals: {
          username: username ? username : '',
          displayDeactivateDialogOption: displayDeactivateDialogOption,
          currentLanguage: LanguageService.getCurrentInstantly()
        }
      });
    };

    return {
      display: display
    };
  });
