/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('AccessWaySearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout', 'CurrentProjectService',
    function($scope, VariableSearchService, $timeout, CurrentProjectService) {
      // prevent access-way changed events during init
      var initializing = true;
      var selectionChanging = false;
      var lastSearchText;
      var lastFilter;
      var lastProjectId;
      var lastSearchResult;
      var init = function() {
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['access-way']) {
          $scope.searchAccessWays(
            $scope.currentSearchParams.filter['access-way']).then(
              function(accessWays) {
                if (accessWays.length === 1) {
                  $scope.currentAccessWay = accessWays[0];
                } else {
                  $scope.currentAccessWay =
                    $scope.currentSearchParams.filter['access-way'];
                  $timeout(function() {
                    $scope.accessWayFilterForm.accessWayFilter.$setValidity(
                      'md-require-match', false);
                  }, 500);
                  $scope.accessWayFilterForm.accessWayFilter.$setTouched();
                }
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(accessWay) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (accessWay) {
          $scope.currentSearchParams.filter['access-way'] = accessWay;
        } else {
          delete $scope.currentSearchParams.filter['access-way'];
        }
        $scope.accessWayChangedCallback();
      };

      $scope.searchAccessWays = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'access-way');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
            CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
           lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return VariableSearchService.findAccessWays(
          searchText, cleanedFilter, currentProjectId)
          .then(function(accessWays) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastProjectId = currentProjectId;
            lastSearchResult = accessWays;
            return accessWays;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter["access-way"]',
        function() {
          init();
        });
    }
  ]);
