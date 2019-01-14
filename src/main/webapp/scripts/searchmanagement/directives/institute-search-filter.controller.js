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
        var language = LanguageService.getCurrentInstantly();
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'institution-de', 'institution-en');

        return StudySearchService.findInstitutions(searchText, cleanedFilter,
          language, true);
      };
    });
