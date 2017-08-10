/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'SearchFilterHelperService', '$timeout', 'StudyIdBuilderService',
    function($scope, SearchFilterHelperService, $timeout,
      StudyIdBuilderService) {
      var elasticSearchTypeChanged = false;
      $scope.$watch('currentElasticsearchType', function() {
        elasticSearchTypeChanged = true;
        $scope.availableFilters = SearchFilterHelperService.getAvailableFilters(
          $scope.currentElasticsearchType);

        $scope.selectedFilters = [];
        if ($scope.currentSearchParams.filter) {
          $scope.selectedFilters = _.intersection(
            _.keys($scope.currentSearchParams.filter), $scope.availableFilters);
        }
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

      $scope.$on('current-project-changed',
          function(event, currentProject) { // jshint ignore:line
            if (currentProject) {
              if (!$scope.currentSearchParams.filter) {
                $scope.currentSearchParams.filter = {};
              }
              $scope.currentSearchParams.filter.study =
                StudyIdBuilderService.buildStudyId(currentProject.id);
              if (!_.includes($scope.selectedFilters, 'study')) {
                $timeout(function() {
                  $scope.selectedFilters.push('study');
                });
              }
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
