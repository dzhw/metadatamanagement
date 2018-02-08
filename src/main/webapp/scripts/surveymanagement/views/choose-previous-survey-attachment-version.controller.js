/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousSurveyAttachmentVersionController',
    function(SurveyAttachmentVersionsResource, surveyId, filename,
      $scope, $mdDialog, LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        surveyId: surveyId,
        filename: filename
      };

      $scope.getSurveyAttachmentVersions = function() {
        SurveyAttachmentVersionsResource.get({
          surveyId: surveyId,
          filename: filename,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(attachments) {
              $scope.surveyAttachments = attachments;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(surveyAttachment, index) {
        $mdDialog.hide({
          surveyAttachment: surveyAttachment,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getSurveyAttachmentVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getSurveyAttachmentVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'survey-management' +
            '.detail.attachments.choose-previous-version' +
            '.current-version-tooltip');
        }
      };

      $scope.getSurveyAttachmentVersions();
    });
