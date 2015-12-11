'use strict';

angular.module('metadatamanagementApp').controller('SurveyDialogController', [
  '$scope', '$stateParams', '$uibModalInstance',
    'entity', 'isCreateMode', 'Survey',
  function($scope, $stateParams, $uibModalInstance,
    entity, isCreateMode, Survey) {
    $scope.isCreateMode = isCreateMode;
    $scope.survey = entity;
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
