'use strict';

angular.module('metadatamanagementApp').controller('HealthModalController',
    function($scope, $modalInstance, currentHealth, baseName, subSystemName) {

      $scope.currentHealth = currentHealth;
      $scope.baseName = baseName, $scope.subSystemName = subSystemName;

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };
    });
