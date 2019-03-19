/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousDataSetVersionController',
    function(DataSetVersionsResource, dataSetId, $scope, $mdDialog,
      LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        dataSetId: dataSetId
      };

      $scope.getDataSetVersions = function() {
        DataSetVersionsResource.get({
          id: dataSetId,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(dataSets) {
              $scope.dataSets = dataSets;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(dataSet, index) {
        delete dataSet.version;
        $mdDialog.hide({
          dataSet: dataSet,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getDataSetVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getDataSetVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'data-set-management' +
            '.edit.choose-previous-version.current-version-tooltip');
        }
      };

      $scope.getDataSetVersions();
    });
