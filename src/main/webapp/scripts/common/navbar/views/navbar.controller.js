'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isAdminMenuOpen = false;
    $scope.isEntityMenuOpen = false;
    $scope.isAccountMenuOpen = false;

    //Functions for toggling buttons.
    $scope.toggleEntityMenu = function() {
      $scope.isEntityMenuOpen = !$scope.isEntityMenuOpen;
    };

    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

  });
