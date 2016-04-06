'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDetailController',
    function($scope, entity, UploadService) {
      $scope.dataAcquisitionProject = entity;
      $scope.uploadState = UploadService.getUploadState;
      $scope.uploadDataSets = function(file) {
        UploadService.uploadDataSets(file,$scope.dataAcquisitionProject.id);
      };
      $scope.uploadSurveys = function(file) {
        UploadService.uploadSurveys(file,$scope.dataAcquisitionProject.id);
      };
      $scope.uploadAtomicQuestions = function(file) {
        UploadService.uploadAtomicQuestions(file,
          $scope.dataAcquisitionProject.id);
      };
      $scope.uploadVariables = function(file) {
        UploadService.uploadVariables(file,$scope.dataAcquisitionProject.id);
      };
      /*
      JobLog, UploadDataSet
      $scope.color = 'md-primary';
      $scope.uploadModus = false;
      $scope.uploadDataSets = function(file) {
        $scope.uploadModus = true;
        JobLog.start('Test');
        UploadDataSet.upload(file,$scope.dataAcquisitionProject.id);
      };*/
      $scope.$watch('uploadState.progress', function() {
        if ($scope.uploadState.hasFinished) {
          $scope.$broadcast($scope.uploadState.typeOfDomainObject +
            '-list-uploaded');
        }
      });
    });
