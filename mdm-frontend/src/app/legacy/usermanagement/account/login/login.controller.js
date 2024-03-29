'use strict';

angular.module('metadatamanagementApp').controller('LoginController', [
  '$rootScope',
  '$scope',
  '$state',
  '$timeout',
  'Auth',
  'PageMetadataService',
  'LanguageService',
  'BreadcrumbService',
  function($rootScope, $scope, $state, $timeout, Auth, PageMetadataService,
    LanguageService, BreadcrumbService) {
    PageMetadataService.setPageTitle('user-management.login.title');
    $scope.user = {};
    $scope.errors = {};
    $scope.rememberMe = true;
    $timeout(function() {
      angular.element('[ng-model="username"]').focus();
    });
    $scope.login = function(event) {
      event.preventDefault();
      Auth.login({
        username: $scope.username,
        password: $scope.password,
        rememberMe: $scope.rememberMe
      }).then(function() {
        $scope.authenticationError = false;
        if ($rootScope.previousStateName === 'register') {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly()
          });
        } else {
          $rootScope.back();
        }
      }).catch(function() {
        $scope.authenticationError = true;
      });
    };
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  }]);

