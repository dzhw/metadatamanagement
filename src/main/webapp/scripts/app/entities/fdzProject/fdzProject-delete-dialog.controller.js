'use strict';

angular.module('metadatamanagementApp')
	.controller('FdzProjectDeleteController',
		function($scope, $uibModalInstance, entity) {

  $scope.fdzProject = entity;
  $scope.clear = function() {
    $uibModalInstance.dismiss('cancel');
  };
  $scope.confirmDelete = function() {
    $scope.fdzProject.$delete().then(function() {
      $uibModalInstance.close(true);
    });
  };

});
