'use strict';

angular.module('metadatamanagementApp')
	.controller('SurveyDeleteController', function($scope, $uibModalInstance,
		entity, Survey) {
  $scope.survey = entity;
  $scope.clear = function() {
    $uibModalInstance.dismiss('cancel');
  };
  $scope.confirmDelete = function(id) {
    Survey.delete({
      id: id
    },
				function() {
  $uibModalInstance.close(true);
				});
  };
	});
