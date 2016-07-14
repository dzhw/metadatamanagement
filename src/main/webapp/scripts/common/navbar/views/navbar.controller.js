/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

  });
