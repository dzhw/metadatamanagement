'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousSurveyVersionController',
    function(SurveyVersionsResource, surveyId, $scope, $mdDialog,
      LanguageService, $translate) {
      $scope.currentPage = {
        number: 0,
        limit: 5,
        skip: 0
      };
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      $scope.translationParams = {
        surveyId: surveyId
      };

      $scope.getSurveyVersions = function() {
        SurveyVersionsResource.get({
          id: surveyId,
          limit: $scope.currentPage.limit,
          skip: $scope.currentPage.skip
        }).$promise.then(
            function(surveys) {
              $scope.surveys = surveys;
            });
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.select = function(survey, index) {
        $mdDialog.hide({
          survey: survey,
          isCurrentVersion: $scope.isCurrentVersion(index)
        });
      };

      $scope.nextPage = function() {
        $scope.currentPage.number++;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getSurveyVersions();
      };

      $scope.previousPage = function() {
        $scope.currentPage.number--;
        $scope.currentPage.skip = ($scope.currentPage.limit *
          $scope.currentPage.number);
        $scope.getSurveyVersions();
      };

      $scope.isCurrentVersion = function(index) {
        return (index === 0 && $scope.currentPage.number === 0);
      };

      $scope.getVersionTitle = function(index) {
        if ($scope.isCurrentVersion(index)) {
          return $translate.instant(
            'survey-management' +
            '.edit.choose-previous-version.current-version-tooltip');
        }
      };

      $scope.getSurveyVersions();
    });
