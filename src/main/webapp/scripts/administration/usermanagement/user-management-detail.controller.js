'use strict';

angular.module('metadatamanagementApp').controller(
  'UserManagementDetailController',
  function($scope, $stateParams, UserResource, PageTitleService, $translate,
    $state, BreadcrumbService) {
    $translate('user-management.detail.title').then(function(title) {
      PageTitleService.setPageTitle(title + ' ' + $stateParams.login);
    });
    $scope.user = {};
    $scope.load = function(login) {
      UserResource.get({
        login: login
      }, function(result) {
        $scope.user = result;
      });
    };
    $scope.load($stateParams.login);
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
