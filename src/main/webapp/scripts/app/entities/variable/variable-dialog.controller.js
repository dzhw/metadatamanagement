'use strict';

angular.module('metadatamanagementApp').controller('VariableDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'isCreateMode',
     'Variable', 'FdzProject',
        function($scope, $stateParams, $modalInstance, entity, isCreateMode,
          Variable, FdzProject) {

          $scope.variable = entity;
          $scope.isCreateMode = isCreateMode;
          $scope.allFdzProjects = FdzProject.query({getAll: 'true'},
            function(result) {
            return result;
          });
          $scope.load = function(id) {
            Variable.get({id: id}, function(result) {
              $scope.variable = result;
            });
          };

          var onSaveFinished = function(result) {
            $scope.$emit('metadatamanagementApp:variableUpdate', result);
            $modalInstance.close(result);
          };

          $scope.save = function() {
            if (isCreateMode) {
              Variable.create($scope.variable, onSaveFinished);
              $scope.$broadcast('variable.created');
            } else {
              Variable.update($scope.variable, onSaveFinished);
              $scope.$broadcast('variable.updated');
            }
          };

          $scope.clear = function() {
            $modalInstance.dismiss('cancel');
          };
        }
      ]
    );
