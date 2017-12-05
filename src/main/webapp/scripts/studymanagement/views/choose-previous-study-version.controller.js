'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousStudyVersionController',
    function(StudyVersionsResource, studyId, $scope, $mdDialog,
      LanguageService) {
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 1
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.studyId = {
        studyId: studyId
      };

      $scope.getStudyVersions = function() {
        StudyVersionsResource.get({
          id: studyId,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(studies) {
              $scope.studies = studies;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(study) {
        $mdDialog.hide(study);
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number) + 1;
        $scope.getStudyVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number) + 1;
        $scope.getStudyVersions();
      };

      $scope.getStudyVersions();
    });
