'use strict';

angular.module('metadatamanagementApp')
	.controller('DataAcquisitionProjectDeleteController',
		function($scope, $uibModalInstance, entity) {

  $scope.dataAcquisitionProject = entity;
  $scope.clear = function() {
    $uibModalInstance.dismiss('cancel');
  };
  $scope.confirmDelete = function() {
    $scope.dataAcquisitionProject.$delete().then(function() {
      $uibModalInstance.close(true);
    });
  };

});
