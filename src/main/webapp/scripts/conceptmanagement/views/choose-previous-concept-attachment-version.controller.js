/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousConceptAttachmentVersionController',
    function(ConceptAttachmentVersionsResource, conceptId, filename,
      $scope, $mdDialog, LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        conceptId: conceptId,
        filename: filename
      };

      $scope.getConceptAttachmentVersions = function() {
        ConceptAttachmentVersionsResource.get({
          conceptId: conceptId,
          filename: filename,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(attachments) {
              $scope.conceptAttachments = attachments;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(conceptAttachment, index) {
        $mdDialog.hide({
          conceptAttachment: conceptAttachment,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getConceptAttachmentVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getConceptAttachmentVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'concept-management' +
            '.detail.attachments.choose-previous-version' +
            '.current-version-tooltip');
        }
      };

      $scope.getConceptAttachmentVersions();
    });
