/* global _ */
'use strict';
angular.module('metadatamanagementApp')
  .controller('SurveyMethodSearchFilterController',
    function($scope, $location, CurrentProjectService, SurveySearchService,
             LanguageService) {
      var termKeyPrefix = 'filter.surveyMethod';
      $scope.searchText = '';

      $scope.onSelectionChanged = function(surveyMethod) {
        var currentLanguage = LanguageService.getCurrentInstantly();
        var path = termKeyPrefix + '-' + LanguageService.getCurrentInstantly();

        if (surveyMethod) {
          var term = surveyMethod[currentLanguage];
          _.set($scope.currentSearchParams, path, term);
        } else {
          _.unset($scope.currentSearchParams, 'filter.surveyMethod-de');
          _.unset($scope.currentSearchParams, 'filter.surveyMethod-en');
        }

        $scope.surveyMethodChangedCallback();
      };

      $scope.searchSurveyMethods = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'survey-method-de', 'survey-method-en');

        return SurveySearchService.findSurveyMethodFilterOptions(searchText,
          cleanedFilter, $scope.type, $scope.query, $scope.projectId);
      };
    });
