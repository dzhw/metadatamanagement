/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousConceptVersionController',
    function(ConceptVersionsResource, conceptId, $scope, $mdDialog,
      LanguageService, $translate) {
      $scope.bowser = bowser;
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        conceptId: conceptId
      };

      $scope.getConceptVersions = function() {
        ConceptVersionsResource.get({
          id: conceptId,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(concepts) {
              $scope.concepts = concepts;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(concept, index) {
        if (!concept.tags) {
          concept.tags = {
            de: [],
            en: []
          };
        }
        if (!concept.tags.de) {
          concept.tags.de = [];
        }
        if (!concept.tags.en) {
          concept.tags.en = [];
        }
        $mdDialog.hide({
          concept: concept,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getConceptVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getConceptVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'concept-management' +
            '.edit.choose-previous-version.current-version-tooltip');
        }
      };

      $scope.getConceptVersions();
    });
