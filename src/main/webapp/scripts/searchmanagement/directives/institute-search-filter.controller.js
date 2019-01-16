/* global _ */
'use strict';
angular.module('metadatamanagementApp')
  .controller('InstituteSearchFilterController',
    function($scope, $location, CurrentProjectService, StudySearchService,
             LanguageService) {
      var termKeyPrefix = 'filter.institution';
      $scope.searchText = '';

      $scope.onSelectionChanged = function(institute) {
        var currentLanguage = LanguageService.getCurrentInstantly();
        var path = termKeyPrefix + '-' + LanguageService.getCurrentInstantly();

        if (institute) {
          var term = institute[currentLanguage];
          _.set($scope.currentSearchParams, path, term);
        } else {
          _.unset($scope.currentSearchParams, 'filter.institution-de');
          _.unset($scope.currentSearchParams, 'filter.institution-en');
        }

        $scope.institutionChangedCallback();
      };

      $scope.searchInstitutions = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'institution-de', 'institution-en');

        return StudySearchService.findInstitutionLabels(searchText,
          cleanedFilter, $scope.type, $scope.query, $scope.projectId);
      };
    });