'use strict';

angular.module('metadatamanagementApp')
    .controller('DataAcquisitionProjectDeleteController',
    function($scope, $uibModalInstance, entity) {
    $scope.dataAcquisitionProject = entity;
    $scope.clear = function() {
      $uibModalInstance.dismiss('cancel');
    };
    $scope.confirmDelete = function() {
      $scope.deleting = true;
      $scope.disabled = true;
      $scope.dataAcquisitionProject.$delete().then(function() {
        $scope.deleting = false;
        $scope.disabled = false;
        $uibModalInstance.close(true);
      });
    };
  });
