'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
    function($scope, $location, $state, $translate, Auth, Principal, ENV) {
      var currentPath = $location.path();
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.$state = $state;
      $scope.inProductionOrDev = ENV === 'prod' || ENV === 'dev';

      if (currentPath.indexOf('/de/') >= 0) {
        $translate.use('de');
      }
      if (currentPath.indexOf('/en/') >= 0) {
        $translate.use('en');
      }

      $scope.logout = function() {
        Auth.logout();
        $state.go('home');
      };
    });
