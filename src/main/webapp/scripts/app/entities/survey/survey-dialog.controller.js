'use strict';

angular.module('metadatamanagementApp').controller('SurveyDialogController', [
  '$scope', '$stateParams', '$uibModalInstance',
    'entity', 'isCreateMode', 'Survey','FdzProject',
  function($scope, $stateParams, $uibModalInstance,
    entity, isCreateMode, Survey, FdzProject) {
    $scope.isCreateMode = isCreateMode;
    $scope.allFdzProjects = FdzProject.query(function(response) {
      $scope.allFdzProjects = response._embedded.fdzProjects;
    });
    if (isCreateMode) {
      $scope.survey = new Survey();
    } else {
      $scope.survey = entity;
    }

    var onSaveFinished = function(result) {
        $uibModalInstance.close(result);
        $scope.isSaving = false;
      };

    var onSaveError = function() {
        $scope.isSaving = false;
      };

    $scope.save = function() {
        $scope.isSaving = true;
        if (isCreateMode) {
          $scope.survey.$create(onSaveFinished, onSaveError);
        } else {
          $scope.survey.$update(onSaveFinished, onSaveError);
        }
      };

    $scope.clear = function() {
        $uibModalInstance.dismiss('cancel');
      };
  }
]);
