'use strict';

angular.module('metadatamanagementApp').controller('HomeController',
  function($scope, Principal) {
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
    });
  });
