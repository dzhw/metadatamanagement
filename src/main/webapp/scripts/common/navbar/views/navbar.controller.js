'use strict';

angular.module('metadatamanagementApp').controller(
  'NavbarController',
  function($scope, Principal, Auth, $state) {
    $scope.isAuthenticated = Principal.isAuthenticated;
    //Logout function
    $scope.logout = function() {
      Auth.logout();
      $state.go('home');
    };
  });
