'use strict';

angular.module('metadatamanagementApp').controller('ResetFinishController',
  function($scope, $stateParams, $timeout, Auth, $translate,
    PageTitleService) {
    $translate('user-management.reset.finish.title').then(
      PageTitleService.setPageTitle);

    $scope.keyMissing = $stateParams.key === undefined;
    $scope.doNotMatch = null;

    $scope.resetAccount = {};
    $timeout(function() {
      angular.element('[ng-model="resetAccount.password"]').focus();
    });

    $scope.finishReset = function() {
      if ($scope.resetAccount.password !== $scope.confirmPassword) {
        $scope.doNotMatch = 'ERROR';
      } else {
        Auth.resetPasswordFinish({
          key: $stateParams.key,
          newPassword: $scope.resetAccount.password
        }).then(function() {
          $scope.success = 'OK';
        }).catch(function() {
          $scope.success = null;
          $scope.error = 'ERROR';
        });
      }

    };
  });
