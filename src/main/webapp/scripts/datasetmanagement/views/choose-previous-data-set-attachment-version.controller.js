/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousDataSetAttachmentVersionController',
    function(DataSetAttachmentVersionsResource, dataSetId, filename,
      $scope, $mdDialog, LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        dataSetId: dataSetId,
        filename: filename
      };

      $scope.getDataSetAttachmentVersions = function() {
        DataSetAttachmentVersionsResource.get({
          dataSetId: dataSetId,
          filename: filename,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(attachments) {
              $scope.dataSetAttachments = attachments;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(dataSetAttachment, index) {
        $mdDialog.hide({
          dataSetAttachment: dataSetAttachment,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getDataSetAttachmentVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getDataSetAttachmentVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'data-set-management' +
            '.detail.attachments.choose-previous-version' +
            '.current-version-tooltip');
        }
      };

      $scope.getDataSetAttachmentVersions();
    });
