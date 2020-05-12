'use strict';

angular.module('metadatamanagementApp').controller('ResetFinishController',
  function($scope, $state, $stateParams, $timeout, Auth, BreadcrumbService,
    PageTitleService) {
    PageTitleService.setPageTitle('user-management.reset.finish.title');

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
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
