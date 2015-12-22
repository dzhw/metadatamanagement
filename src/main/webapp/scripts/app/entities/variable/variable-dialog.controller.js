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

          $scope.isSurveyEmpty = function() {

            //check for array
            if (!angular.isArray($scope.allSurveysByFdzProjectName)) {
              return true;
            }

            //check for array size
            if ($scope.allSurveysByFdzProjectName.length === 0) {
              return true;
            }

            return false;
          };

          $scope.changeSurvey = function() {

            //query for survey with a given fdz project name
            $scope.allSurveysByFdzProjectName =
            Survey.query({fdzProjectName: $scope.variable.fdzProjectName},
                  function(result) {
                    //return the array of surveys.
                    return result;
                  });
          };

          $scope.clear = function() {
            $modalInstance.dismiss('cancel');
          };
        }
      ]
    );
