'use strict';

angular.module('metadatamanagementApp').controller('SurveyDialogController', [
  '$scope', '$stateParams', '$uibModalInstance',
  'entity', 'isCreateMode', 'DataAcquisitionProjectCollection',
  function($scope, $stateParams, $uibModalInstance,
    entity, isCreateMode, DataAcquisitionProjectCollection) {
    $scope.survey = entity;
    $scope.isCreateMode = isCreateMode;
    $scope.allDataAcquisitionProjects = DataAcquisitionProjectCollection.query(
      function(response) {
        $scope.allDataAcquisitionProjects =
          response._embedded.dataAcquisitionProjects;
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
