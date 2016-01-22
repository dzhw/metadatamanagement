'use strict';

angular.module('metadatamanagementApp').controller('SurveyDialogController', [
  '$scope', '$stateParams', '$uibModalInstance',
    'entity', 'isCreateMode', 'FdzProjectCollection',
  function($scope, $stateParams, $uibModalInstance,
    entity, isCreateMode, FdzProjectCollection) {
    $scope.survey = entity;
    $scope.isCreateMode = isCreateMode;
    $scope.allFdzProjects = FdzProjectCollection.query(function(response) {
      $scope.allFdzProjects = response._embedded.fdzProjects;
    });

    var onSaveFinished = function(result) {
        $uibModalInstance.close(result);
        $scope.isSaving = false;
      };

    var onSaveError = function() {
        $scope.isSaving = false;
      };

    $scope.save = function() {
        $scope.isSaving = true;
        $scope.survey.$save(onSaveFinished, onSaveError);
      };

    $scope.clear = function() {
        $uibModalInstance.dismiss('cancel');
      };
  }
]);
