'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDialogController', ['$scope',
    '$stateParams', '$uibModalInstance', 'entity',
    'DataAcquisitionProject', 'isCreateMode',
    function($scope, $stateParams, $uibModalInstance, entity,
      DataAcquisitionProject, isCreateMode) {
      $scope.isCreateMode = isCreateMode;
      $scope.dataAcquisitionProject = entity;

      var onSaveFinished = function(result) {
        $uibModalInstance.close(result);
        $scope.isSaving = false;
      };

      var onSaveError = function() {
        $scope.isSaving = false;
      };

      $scope.save = function() {
        $scope.isSaving = true;
        $scope.dataAcquisitionProject.$save(onSaveFinished, onSaveError);
      };

      $scope.clear = function() {
        $uibModalInstance.dismiss('cancel');
      };
    }
  ]);
