'use strict';

angular.module('metadatamanagementApp').controller('FdzProjectDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FdzProject',
        function($scope, $stateParams, $uibModalInstance, entity, FdzProject) {

          $scope.fdzProject = entity;
          $scope.load = function(name) {
            FdzProject.get({name: name}, function(result) {
              $scope.fdzProject = result;
            });
          };

          var onSaveSuccess = function(result) {
            $scope.$emit('metadatamanagementApp:fdzProjectUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
          };

          var onSaveError = function() {
            $scope.isSaving = false;
          };

          $scope.save = function() {
            $scope.isSaving = true;
            //TODO change to create and update mode
            if ($scope.fdzProject.name !== null) {
              FdzProject.update($scope.fdzProject, onSaveSuccess, onSaveError);
            } else {
              FdzProject.save($scope.fdzProject, onSaveSuccess, onSaveError);
            }
          };

          $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
          };
        }]);
