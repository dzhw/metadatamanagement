'use strict';

angular.module('metadatamanagementApp').controller('VariableDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'isCreateMode',
     'Variable', 'FdzProject', 'Survey',
        function($scope, $stateParams, $modalInstance, entity, isCreateMode,
          Variable, FdzProject, Survey) {

          $scope.variable = entity;
          $scope.isCreateMode = isCreateMode;
          $scope.allFdzProjects = FdzProject.query({getAll: 'true'},
            function(result) {
            return result;
          });

          $scope.allSurveysByFdzProjectName = null;

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

          $scope.changeSurvey = function() {
            $scope.allSurveysByFdzProjectName =
            Survey.query({fdzProjectName: $scope.variable.fdzProjectName},
                  function(result) {
                    return result;
                  });
          };

          $scope.clear = function() {
            $modalInstance.dismiss('cancel');
          };
        }
      ]
    );
