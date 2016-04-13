'use strict';

angular.module('metadatamanagementApp').controller('VariableDialogController', [
  '$scope', '$stateParams', '$uibModalInstance', 'entity', 'isCreateMode',
  'DataAcquisitionProjectCollection', 'SurveyCollection',
  function($scope, $stateParams, $uibModalInstance, entity, isCreateMode,
    DataAcquisitionProjectCollection, SurveyCollection) {

    $scope.variable = entity;
    $scope.isCreateMode = isCreateMode;
    //TODO load all page by page
    $scope.allDataAcquisitionProjects = DataAcquisitionProjectCollection.query(
      function(response) {
        $scope.allDataAcquisitionProjects =
          response._embedded.dataAcquisitionProjects;
      });

    $scope.allSurveysByDataAcquisitionProjectId = null;

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
      if (!angular.isArray($scope.allSurveysByDataAcquisitionProjectId)) {
        return true;
      }

      //check for array size
      if ($scope.allSurveysByDataAcquisitionProjectId.length === 0) {
        return true;
      }

      return false;
    };

    $scope.changeSurvey = function() {

      //query for survey with a given rdc project name
      // TODO load all page by page
      SurveyCollection.query({
          'dataAcquisitionProjectId': $scope.variable.dataAcquisitionProjectId
        },
        function(result) {
          //return the array of surveys.
          $scope.allSurveysByDataAcquisitionProjectId = result._embedded
            .surveys;
        });
    };

    if (!isCreateMode) {
      $scope.changeSurvey();
    }

    $scope.clear = function() {
      $uibModalInstance.dismiss('cancel');
    };
  }
]);
