'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousStudyAttachmentVersionController',
    function(StudyAttachmentVersionsResource, studyId, filename,
      $scope, $mdDialog, LanguageService, $translate) {
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        studyId: studyId,
        filename: filename
      };

      $scope.getStudyAttachmentVersions = function() {
        StudyAttachmentVersionsResource.get({
          studyId: studyId,
          filename: filename,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(attachments) {
              $scope.studyAttachments = attachments;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(studyAttachment, index) {
        $mdDialog.hide({
          studyAttachment: studyAttachment,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getStudyAttachmentVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getStudyAttachmentVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'study-management' +
            '.detail.attachments.choose-previous-version' +
            '.current-version-tooltip');
        }
      };

      $scope.getStudyAttachmentVersions();
    });
