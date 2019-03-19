/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousInstrumentVersionController',
    function(InstrumentVersionsResource, instrumentId, $scope, $mdDialog,
      LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        instrumentId: instrumentId
      };

      $scope.getInstrumentVersions = function() {
        InstrumentVersionsResource.get({
          id: instrumentId,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(instruments) {
              $scope.instruments = instruments;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(instrument, index) {
        delete instrument.version;
        $mdDialog.hide({
          instrument: instrument,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getInstrumentVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getInstrumentVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'instrument-management' +
            '.edit.choose-previous-version.current-version-tooltip');
        }
      };

      $scope.getInstrumentVersions();
    });
