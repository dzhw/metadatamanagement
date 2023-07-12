/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
    .controller('ChoosePreviousAttachmentVersionController',
        function(getAttachmentVersionsCallback, domainId, filename, $scope,
                 $mdDialog, LanguageService, $translate) {

          $scope.bowser = bowser;
          $scope.currentPage = {
            number: 0,
            limit: 5,
            skip: 0
          };
          $scope.currentLanguage = LanguageService.getCurrentInstantly();
          $scope.translationParams = {
            filename: filename
          };

          $scope.getAttachmentVersions = function() {
            getAttachmentVersionsCallback(domainId, filename,
                $scope.currentPage.limit,
                $scope.currentPage.skip).then(
                function(attachments) {
                  $scope.attachments = attachments;
                });
          };

          $scope.closeDialog = function() {
            $mdDialog.cancel();
          };

          $scope.select = function(attachment, index) {
            $mdDialog.hide({
              dataPackageAttachment: attachment,
              isCurrentVersion: $scope.isCurrentVersion(index)
            });
          };

          $scope.nextPage = function() {
            $scope.currentPage.number++;
            $scope.currentPage.skip = ($scope.currentPage.limit *
                $scope.currentPage.number);
            $scope.getAttachmentVersions();
          };

          $scope.previousPage = function() {
            $scope.currentPage.number--;
            $scope.currentPage.skip = ($scope.currentPage.limit *
                $scope.currentPage.number);
            $scope.getAttachmentVersions();
          };

          $scope.isCurrentVersion = function(index) {
            return (index === 0 && $scope.currentPage.number === 0);
          };

          $scope.getVersionTitle = function(index) {
            if ($scope.isCurrentVersion(index)) {
              return $translate.instant(
                  'choose-previous-attachment-version' +
                  '.current-version-tooltip');
            }
          };

          $scope.getAttachmentVersions();
        });
