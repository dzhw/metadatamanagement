'use strict';

angular.module('metadatamanagementApp').controller('PasswordController',
  function($scope, Auth, Principal, PageTitleService, $translate) {
    $translate('global.menu.account.password').then(
      PageTitleService.setPageTitle);
    Principal.identity().then(function(account) {
      $scope.account = account;
    });

    $scope.success = null;
    $scope.error = null;
    $scope.doNotMatch = null;
    $scope.changePassword = function() {
      if ($scope.password !== $scope.confirmPassword) {
        $scope.doNotMatch = 'ERROR';
      } else {
        $scope.doNotMatch = null;
        Auth.changePassword($scope.password).then(function() {
          $scope.error = null;
          $scope.success = 'OK';
        }).catch(function() {
          $scope.success = null;
          $scope.error = 'ERROR';
        });
      }
    };
  });
