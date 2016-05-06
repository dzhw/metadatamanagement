'use strict';

angular.module('metadatamanagementApp').controller(
    'UserManagementDetailController', function($scope, $stateParams,
      UserResource) {
      $scope.user = {};
      $scope.load = function(login) {
        UserResource.get({
          login: login
        }, function(result) {
          $scope.user = result;
        });
      };
      $scope.load($stateParams.login);
    });
