/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'SearchFilterHelperService', '$timeout',
    function($scope, SearchFilterHelperService, $timeout) {
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
    }
  ]);
