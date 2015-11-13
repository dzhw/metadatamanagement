'use strict';

angular.module('metadatamanagementApp').controller('LoginController',
    function($rootScope, $location, $scope, $state, $timeout,
      BookmarkableUrl, Auth) {
      $scope.user = {};
      $scope.errors = {};
      BookmarkableUrl.setUrlLanguage($location, $rootScope);
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
            $state.go('home');
          } else {
            $rootScope.back();
          }
        }).catch(function() {
          $scope.authenticationError = true;
        });
      };
    });
