'use strict';

angular.module('metadatamanagementApp').controller('LoginController',
  function($scope, $state, $timeout, Auth, PageMetadataService,
           BreadcrumbService) {
    PageMetadataService.setPageTitle('user-management.login.title');
    $scope.user = {};
    $scope.errors = {};
    $scope.rememberMe = true;
    $timeout(function() {
      angular.element('[ng-model="username"]').focus();
    });
    $scope.login = function() {
      Auth.login().catch(function() {
        $scope.authenticationError = true;
      });
    };
    BreadcrumbService.updateToolbarHeader({
      'stateName': $state.current.name
    });
  });
