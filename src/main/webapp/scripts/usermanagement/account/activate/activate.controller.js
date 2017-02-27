'use strict';

angular.module('metadatamanagementApp').controller('ActivationController',
  function($scope, $state, $stateParams, Auth, PageTitleService,
    ToolbarHeaderService) {
    PageTitleService.setPageTitle('user-management.activate.title');
    Auth.activateAccount({
      key: $stateParams.key
    }).then(function() {
      $scope.error = null;
      $scope.success = 'OK';
    }).catch(function() {
      $scope.success = null;
      $scope.error = 'ERROR';
    });
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
