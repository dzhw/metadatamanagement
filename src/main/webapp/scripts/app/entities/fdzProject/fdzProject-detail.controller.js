'use strict';

angular.module('metadatamanagementApp')
    .controller('FdzProjectDetailController',
      function($scope, $rootScope, $stateParams, entity, FdzProject) {
      $scope.fdzProject = entity;
      $scope.load = function(name) {
        FdzProject.get({name: name}, function(result) {
          $scope.fdzProject = result;
        });
      };
      var unsubscribe = $rootScope.$on(
        'metadatamanagementApp:fdzProjectUpdate', function(event, result) {
        $scope.fdzProject = result;
      });
      $scope.$on('$destroy', unsubscribe);

    });
