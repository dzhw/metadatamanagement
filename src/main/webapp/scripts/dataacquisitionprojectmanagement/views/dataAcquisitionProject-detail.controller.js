'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDetailController',
    function(CustomModalService, $scope, entity, JobLoggingService,
      DataSetUploadService, $translate,
      DataAcquisitionProjectPostValidationService,
      VariableUploadService,
      SurveyUploadService, AtomicQuestionUploadService) {
      $scope.dataAcquisitionProject = entity;
      $scope.objLists = {
        dataSetList: {},
        surveyList: {},
        variableList: {},
        atomicQuestionList: {}
      };

      $scope.job = JobLoggingService.getCurrentJob();
      $scope.uploadSurveys = function(file) {
        if (file !== null) {
          CustomModalService.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteSurveys', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              SurveyUploadService
                .uploadSurveys(file, $scope.dataAcquisitionProject.id);
            }
          });
        }
      };
      $scope.uploadAtomicQuestions = function(file) {
        if (file !== null) {
          CustomModalService.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteAtomicQuestions', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              AtomicQuestionUploadService
                .uploadAtomicQuestions(file, $scope.dataAcquisitionProject
                  .id);
            }
          });
        }
      };
      $scope.uploadVariables = function(file) {
        if (file !== null) {
          CustomModalService.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteVariables', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              VariableUploadService
                .uploadVariables(file, $scope.dataAcquisitionProject.id);
            }
          });
        }
      };
      $scope.uploadDataSets = function(file) {
        if (file !== null) {
          CustomModalService.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteDataSets', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              DataSetUploadService.
              uploadDataSets(file, $scope.dataAcquisitionProject.id);
            }
          });
        }
      };
      $scope.postValidate = function() {
        DataAcquisitionProjectPostValidationService
          .postValidate($scope.dataAcquisitionProject.id);
      };
      $scope.$watch('job.state', function() {
        if ($scope.job.state === 'finished') {
          $scope.$broadcast($scope.job.id +
            '-list-uploaded');
        }
      });
    });
