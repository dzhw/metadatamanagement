'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDeleteController', function($scope, $uibModalInstance,
    entity) {
    $scope.survey = entity;
    $scope.clear = function() {
      $uibModalInstance.dismiss('cancel');
    };
    $scope.confirmDelete = function() {
      $scope.survey.$delete().then(function() {
        $uibModalInstance.close(true);
      });
    };
  });
