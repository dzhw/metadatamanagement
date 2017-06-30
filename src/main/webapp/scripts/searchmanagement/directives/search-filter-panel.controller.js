/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'CleanJSObjectService', 'SearchFilterHelperService',
    function($scope, CleanJSObjectService, SearchFilterHelperService) {
      $scope.isFilterActive = !CleanJSObjectService.isNullOrEmpty(
        $scope.currentSearchParams.filter);
      $scope.filterPanelClosed = !$scope.isFilterActive;
      $scope.toggleFilterPanelClosed = function() {
        $scope.filterPanelClosed = !$scope.filterPanelClosed;
      };

      $scope.$watch('currentElasticsearchType', function() {
        $scope.availableFilters = SearchFilterHelperService.getAvailableFilters(
          $scope.currentElasticsearchType);

        $scope.selectedFilters = [];
        $scope.isFilterActive = !CleanJSObjectService.isNullOrEmpty(
            $scope.currentSearchParams.filter);
        if ($scope.isFilterActive) {
          $scope.selectedFilters = _.keys($scope.currentSearchParams.filter);
        }
      });

      $scope.onOneFilterChanged = function() {
        $scope.isFilterActive = !CleanJSObjectService.isNullOrEmpty(
          $scope.currentSearchParams.filter);
        $scope.filterChangedCallback();
      };
    }
  ]);
