'use strict';

angular.module('metadatamanagementApp').controller('HealthModalController', [
  '$scope',
  '$uibModalInstance',
  'currentHealth',
  'baseName',
  'subSystemName',
  function($scope, $uibModalInstance,
    currentHealth, baseName, subSystemName) {

    $scope.currentHealth = currentHealth;
    $scope.baseName = baseName;
    $scope.subSystemName = subSystemName;

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  }]);

