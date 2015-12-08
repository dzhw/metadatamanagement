'use strict';

angular.module('metadatamanagementApp').controller('SettingsController',
    function($scope, $rootScope, $location, Principal,
        Auth, Language) {
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
          Language.getCurrent().then(function(current) {
            if ($scope.settingsAccount.langKey !== current) {
              Language.setCurrent($scope.settingsAccount.langKey);
            }
          });
        }).catch(function() {
          $scope.success = null;
          $scope.error = 'ERROR';
        });
      };
    });
