'use strict';

angular.module('metadatamanagementApp').controller('VariableDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'isCreateMode',
     'Variable', 'FdzProjectCollection', 'SurveyCollection',
        function($scope, $stateParams, $uibModalInstance, entity, isCreateMode,
          Variable, FdzProjectCollection, SurveyCollection) {

          $scope.variable = entity;
          $scope.isCreateMode = isCreateMode;
          //TODO load all page by page
          $scope.allFdzProjects = FdzProjectCollection.query(
            function(response) {
            $scope.allFdzProjects = response._embedded.fdzProjects;
          });

          $scope.allSurveysByFdzProjectName = null;

          $scope.load = function(id) {
            Variable.get({id: id}, function(result) {
              $scope.variable = result;
            });
          };

          var onSaveFinished = function(result) {
            $scope.$emit('metadatamanagementApp:variableUpdate', result);
            $uibModalInstance.close(result);
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
            // TODO load all page by page
            $scope.allSurveysByFdzProjectName = SurveyCollection.query(
              {'fdzProject.name': $scope.variable.fdzProject.name},
                  function(result) {
                    //return the array of surveys.
                    return result._embedded.surveys;
                  });
          };

          $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
          };
        }
      ]
    );
