'use strict';

angular.module('metadatamanagementApp').controller('UserManagementDetailController', [
  '$scope',
  '$stateParams',
  'UserResource',
  'PageMetadataService',
  '$translate',
  '$state',
  'BreadcrumbService',
  function($scope, $stateParams, UserResource, PageMetadataService, $translate,
    $state, BreadcrumbService) {
    $translate('user-management.detail.title').then(function(title) {
      PageMetadataService.setPageTitle(title + ' ' + $stateParams.login);
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
  }]);

