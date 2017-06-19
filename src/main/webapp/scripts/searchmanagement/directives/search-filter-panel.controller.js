/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'CleanJSObjectService',
    function($scope, CleanJSObjectService) {
      $scope.isFilterActive = !CleanJSObjectService.isNullOrEmpty(
        $scope.currentSearchParams.filter);
      $scope.filterPanelClosed = !$scope.isFilterActive;
      $scope.toggleFilterPanelClosed = function() {
        $scope.filterPanelClosed = !$scope.filterPanelClosed;
      };

      $scope.$watch('currentSearchParams.filter', function() {
        $scope.selectedSearchFilters = {};
        $scope.isFilterActive = !CleanJSObjectService.isNullOrEmpty(
          $scope.currentSearchParams.filter);
        if ($scope.isFilterActive) {
          _.forEach($scope.currentSearchParams.filter, function(value, key) {
            $scope.selectedSearchFilters[key] = value;
          });
        }
      });

      $scope.onOneFilterChanged = function() {
        $scope.isFilterActive = !CleanJSObjectService.isNullOrEmpty(
          $scope.currentSearchParams.filter);
        $scope.filterChangedCallback();
      };
    }
  ]);
