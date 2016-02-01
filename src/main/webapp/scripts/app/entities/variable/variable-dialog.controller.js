'use strict';

angular.module('metadatamanagementApp').controller('VariableDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'isCreateMode',
      'FdzProjectCollection', 'SurveyCollection',
        function($scope, $stateParams, $uibModalInstance, entity, isCreateMode,
          FdzProjectCollection, SurveyCollection) {

          $scope.variable = entity;
          $scope.isCreateMode = isCreateMode;
          //TODO load all page by page
          $scope.allFdzProjects = FdzProjectCollection.query(
            function(response) {
            $scope.allFdzProjects = response._embedded.fdzProjects;
          });

          $scope.allSurveysByFdzProjectId = null;

          var onSaveFinished = function(result) {
            $scope.$emit('metadatamanagementApp:variableUpdate', result);
            $uibModalInstance.close(result);
          };

          $scope.save = function() {
            if (isCreateMode) {
              $scope.variable.$save($scope.variable, onSaveFinished);
              $scope.$broadcast('variable.created');
            } else {
              $scope.variable.$save($scope.variable, onSaveFinished);
              $scope.$broadcast('variable.updated');
            }
          };

          $scope.isSurveyEmpty = function() {

            //check for array
            if (!angular.isArray($scope.allSurveysByFdzProjectId)) {
              return true;
            }

            //check for array size
            if ($scope.allSurveysByFdzProjectId.length === 0) {
              return true;
            }

            return false;
          };

          $scope.changeSurvey = function() {

            //query for survey with a given fdz project name
            // TODO load all page by page
            SurveyCollection.query(
              {'fdzProjectId': $scope.variable.fdzProjectId},
                  function(result) {
                    //return the array of surveys.
                    $scope.allSurveysByFdzProjectId = result._embedded.surveys;
                  });
          };

          if (!isCreateMode) {
            $scope.changeSurvey();
          }

          $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
          };
        }
      ]
    );
