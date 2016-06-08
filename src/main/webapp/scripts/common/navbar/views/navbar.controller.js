'use strict';

angular.module('metadatamanagementApp').controller(
  'NavbarController',
  function($scope, $state, Auth, Principal, ENV, $mdSidenav) {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.$state = $state;
    $scope.inProductionOrDev = ENV === 'prod' || ENV === 'dev';

    //Logout function
    $scope.logout = function() {
      Auth.logout();
      $state.go('home');
    };

    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
  });
