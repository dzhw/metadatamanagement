/* globals document */
'use strict';
angular.module('metadatamanagementApp').service('WelcomeDialogService', ['$mdDialog', 'LanguageService', '$rootScope', 
  function($mdDialog, LanguageService, $rootScope) {
    var display = function(username, displayDeactivateDialogOption) {
      displayDeactivateDialogOption = angular
        .isDefined(displayDeactivateDialogOption) ?
        displayDeactivateDialogOption : true;

      return $mdDialog.show({
        controller: 'WelcomeDialogController',
        templateUrl: 'scripts/usermanagement/welcome/views/' +
          'welcome-dialog.html.tmpl',
        parent: angular.element(document.body),
        closeTo: '#fdz-welcome-dialog-item',
        clickOutsideToClose: false,
        fullscreen: true,
        locals: {
          username: username ? username : '',
          displayDeactivateDialogOption: displayDeactivateDialogOption,
          currentLanguage: LanguageService.getCurrentInstantly(),
          bowser: $rootScope.bowser
        }
      });
    };

    return {
      display: display
    };
  }]);

