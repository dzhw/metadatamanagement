'use strict';

angular.module('metadatamanagementApp').controller('ActivationController',
  function($scope, $stateParams, Auth, PageTitleService, $translate) {
    $translate('user-management.activate.title').then(
      PageTitleService.setPageTitle);
    Auth.activateAccount({
      key: $stateParams.key
    }).then(function() {
      $scope.error = null;
      $scope.success = 'OK';
    }).catch(function() {
      $scope.success = null;
      $scope.error = 'ERROR';
    });
  });
