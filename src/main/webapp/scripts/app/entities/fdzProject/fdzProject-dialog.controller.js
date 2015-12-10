'use strict';

angular.module('metadatamanagementApp').controller('FdzProjectDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity',
      'FdzProject', 'isCreateMode',
        function($scope, $stateParams, $uibModalInstance, entity,
          FdzProject, isCreateMode) {
          $scope.fdzProject = entity;
          $scope.isCreateMode = isCreateMode;

          $scope.load = function(name) {
            FdzProject.get({name: name}, function(result) {
              $scope.fdzProject = result;
            });
          };

          var onSaveFinished = function(result) {
            $scope.$emit('metadatamanagementApp:fdzProjectUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
          };

          var onSaveError = function() {
            $scope.isSaving = false;
          };

          $scope.save = function() {
            $scope.isSaving = true;
            if (isCreateMode) {
              FdzProject.create($scope.fdzProject, onSaveFinished, onSaveError);
            } else {
              FdzProject.update($scope.fdzProject, onSaveFinished, onSaveError);
            }
          };

          $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
          };
        }]);
