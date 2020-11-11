'use strict';

angular.module('metadatamanagementApp').controller('ActivationController',
  function($scope, $state, $stateParams, Auth, PageMetadataService,
    BreadcrumbService) {
    PageMetadataService.setPageTitle('user-management.activate.title');
    Auth.activateAccount({
      key: $stateParams.key
    }).then(function() {
      $scope.error = null;
      $scope.success = 'OK';
    }).catch(function() {
      $scope.success = null;
      $scope.error = 'ERROR';
    });
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
