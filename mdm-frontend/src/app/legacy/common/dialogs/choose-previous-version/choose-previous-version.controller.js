/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousVersionController', [
  'getPreviousVersionsCallback',
  'domainId',
  'labels',
  '$scope',
  '$mdDialog',
  'versionLabelAttribute',
  'LanguageService',
  '$translate',
    function(getPreviousVersionsCallback, domainId, labels, $scope, $mdDialog,
             versionLabelAttribute, LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        id: domainId
      };
      $scope.labels = labels;
      $scope.versionLabelAttribute = versionLabelAttribute;

      $scope.getVersions = function() {
        getPreviousVersionsCallback(domainId, $scope.currentPage.limit,
            $scope.currentPage.skip).then(
            function(versions) {
              $scope.versions = versions;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(domainObject, index) {
        if (!domainObject.tags) {
          domainObject.tags = {
            de: [],
            en: []
          };
        }
        if (!domainObject.tags.de) {
          domainObject.tags.de = [];
        }
        if (!domainObject.tags.en) {
          domainObject.tags.en = [];
        }
        $mdDialog.hide({
          selection: domainObject,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
            $scope.currentPage.number);
        $scope.getVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
            $scope.currentPage.number);
        $scope.getVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
              'choose-previous-version.current-version-tooltip');
        }
      };

      $scope.getVersions();
    }]);

