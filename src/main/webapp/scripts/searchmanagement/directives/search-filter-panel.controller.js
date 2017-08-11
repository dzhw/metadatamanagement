/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'SearchFilterHelperService', '$timeout', 'StudyIdBuilderService',
    'CurrentProjectService',
    function($scope, SearchFilterHelperService, $timeout,
      StudyIdBuilderService, CurrentProjectService) {
      var elasticSearchTypeChanged = false;

      var selectStudyForProject = function() {
        if (!_.includes($scope.availableFilters, 'study')) {
          return;
        }
        var currentProject = CurrentProjectService.getCurrentProject();
        if (currentProject) {
          if (!$scope.currentSearchParams.filter) {
            $scope.currentSearchParams.filter = {};
          }
          $scope.currentSearchParams.filter.study =
            StudyIdBuilderService.buildStudyId(currentProject.id);
          if (!_.includes($scope.selectedFilters, 'study')) {
            $scope.selectedFilters.push('study');
          }
        }
      };

      $scope.$watch('currentElasticsearchType', function() {
        elasticSearchTypeChanged = true;
        $scope.availableFilters = SearchFilterHelperService.getAvailableFilters(
          $scope.currentElasticsearchType);

        $scope.selectedFilters = [];
        if ($scope.currentSearchParams.filter) {
          $scope.selectedFilters = _.intersection(
            _.keys($scope.currentSearchParams.filter), $scope.availableFilters);
        }
        selectStudyForProject();
        $timeout(function() {
          elasticSearchTypeChanged = false;
        });
      });

      $scope.onOneFilterChanged = function() {
        $scope.filterChangedCallback();
      };

      $scope.$watch('selectedFilters', function(newSelectedFilters,
        oldSelectedFilters) {
        if (elasticSearchTypeChanged) {
          return;
        }
        var unselectedFilters = _.difference(
          oldSelectedFilters, newSelectedFilters);
        if ($scope.currentSearchParams.filter && unselectedFilters.length > 0) {
          unselectedFilters.forEach(function(unselectedFilter) {
            delete $scope.currentSearchParams.filter[unselectedFilter];
          });
          $scope.filterChangedCallback();
        }
      });

      $scope.$on('current-project-changed', function() {
        var currentProject = CurrentProjectService.getCurrentProject();
        if (currentProject) {
          selectStudyForProject();
        } else {
          if (_.includes($scope.selectedFilters, 'study')) {
            _.remove($scope.selectedFilters, function(selectedFilter) {
              return selectedFilter === 'study';
            });
            delete $scope.currentSearchParams.filter.study;
          }
        }
      });
    }
  ]);
