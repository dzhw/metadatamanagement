'use strict';

angular.module('metadatamanagementApp').controller(
  'NavbarController',
  function($scope, $state, Auth, Principal, ENV, $filter) {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.$state = $state;
    $scope.inProductionOrDev = ENV === 'prod' || ENV === 'dev';

    $scope.logout = function() {
      Auth.logout();
      $state.go('home');
    };
  });
