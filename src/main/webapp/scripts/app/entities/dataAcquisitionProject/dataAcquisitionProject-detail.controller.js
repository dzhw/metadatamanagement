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
      $scope.$watch('uploadState.progress', function() {
        if ($scope.uploadState.hasFinished) {
          $scope.$broadcast($scope.uploadState.uploadedDomainObject);
        }
      });
      /*ToDO*/
      /*
      Variables Import,
      Tex Files Download
      */

    });
