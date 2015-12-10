'use strict';

angular.module('metadatamanagementApp')
	.controller('FdzProjectDeleteController',
		function($scope, $uibModalInstance, entity, FdzProject) {

  $scope.fdzProject = entity;
  $scope.clear = function() {
    $uibModalInstance.dismiss('cancel');
  };
  $scope.confirmDelete = function(name) {
    FdzProject.delete({name: name},
                function() {
                  $uibModalInstance.close(true);
                });
  };

});
