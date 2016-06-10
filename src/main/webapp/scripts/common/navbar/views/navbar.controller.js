'use strict';

angular.module('metadatamanagementApp').controller(
  'NavbarController',
  function($scope, Principal) {
    $scope.isAuthenticated = Principal.isAuthenticated;
  });
