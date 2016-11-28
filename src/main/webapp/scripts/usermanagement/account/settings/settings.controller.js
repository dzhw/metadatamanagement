'use strict';

angular.module('metadatamanagementApp').controller('SettingsController',
  function($scope, $rootScope, $location, Principal,
    Auth, LanguageService, PageTitleService) {
    PageTitleService.setPageTitle('global.menu.account.settings');
    $scope.success = null;
    $scope.error = null;
    Principal.identity(true).then(function(account) {
      $scope.settingsAccount = account;
    });

    $scope.save = function() {
      Auth.updateAccount($scope.settingsAccount).then(function() {
        $scope.error = null;
        $scope.success = 'OK';
        Principal.identity().then(function(account) {
          $scope.settingsAccount = account;
        });
        LanguageService.getCurrent().then(function(current) {
          if ($scope.settingsAccount.langKey !== current) {
            LanguageService.setCurrent($scope.settingsAccount.langKey);
          }
        });
      }).catch(function() {
        $scope.success = null;
        $scope.error = 'ERROR';
      });
    };
  });
