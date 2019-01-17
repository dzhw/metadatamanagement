/* global _ */
'use strict';
angular.module('metadatamanagementApp')
  .controller('SponsorSearchFilterController',
    function($scope, $location, CurrentProjectService, StudySearchService,
             LanguageService) {
      var termKeyPrefix = 'filter.sponsor';
      $scope.searchText = '';

      $scope.onSelectionChanged = function(sponsor) {
        var currentLanguage = LanguageService.getCurrentInstantly();
        var path = termKeyPrefix + '-' + LanguageService.getCurrentInstantly();

        if (sponsor) {
          var term = sponsor[currentLanguage];
          _.set($scope.currentSearchParams, path, term);
        } else {
          _.unset($scope.currentSearchParams, 'filter.sponsor-de');
          _.unset($scope.currentSearchParams, 'filter.sponsor-en');
        }

        $scope.sponsorChangedCallback();
      };

      $scope.searchSponsors = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'sponsor-de', 'sponsor-en');

        return StudySearchService.findSponsorFilterOptions(searchText,
          cleanedFilter, $scope.type, $scope.query, $scope.projectId);
      };
    });
