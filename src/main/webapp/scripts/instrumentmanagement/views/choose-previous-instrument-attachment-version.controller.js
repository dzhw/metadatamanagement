/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousInstrumentAttachmentVersionController',
    function(InstrumentAttachmentVersionsResource, instrumentId, filename,
      $scope, $mdDialog, LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        instrumentId: instrumentId,
        filename: filename
      };

      $scope.getInstrumentAttachmentVersions = function() {
        InstrumentAttachmentVersionsResource.get({
          instrumentId: instrumentId,
          filename: filename,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(attachments) {
              $scope.instrumentAttachments = attachments;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(instrumentAttachment, index) {
        $mdDialog.hide({
          instrumentAttachment: instrumentAttachment,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getInstrumentAttachmentVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getInstrumentAttachmentVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'instrument-management' +
            '.detail.attachments.choose-previous-version' +
            '.current-version-tooltip');
        }
      };

      $scope.getInstrumentAttachmentVersions();
    });
