'use strict';

angular.module('metadatamanagementApp').controller('SurveyDialogController', [
  '$scope', '$stateParams', '$uibModalInstance',
    'entity', 'isCreateMode', 'Survey','FdzProject',
  function($scope, $stateParams, $uibModalInstance,
    entity, isCreateMode, Survey, FdzProject) {
    $scope.isCreateMode = isCreateMode;
    $scope.survey = entity;
    $scope.allFdzProjects = FdzProject.query(
      //TODO load all page by page
      {page: 0, size: 50},
    function(result) {
      return result;
    });

    $scope.load = function(id) {
      Survey.get({
        id: id
      }, function(result) {
        $scope.survey = result;
      });
    };

    var onSaveFinished = function(result) {
      $scope.$emit('metadatamanagementApp:surveyUpdate', result);
      $uibModalInstance.close(result);
      $scope.isSaving = false;
    };

    var onSaveError = function() {
      $scope.isSaving = false;
    };

    $scope.save = function() {
      $scope.isSaving = true;
      if (isCreateMode) {
        Survey.create($scope.survey, onSaveFinished, onSaveError);
      } else {
        Survey.update($scope.survey, onSaveFinished, onSaveError);
      }
    };

    $scope.clear = function() {
      $uibModalInstance.dismiss('cancel');
    };
  }
]);
