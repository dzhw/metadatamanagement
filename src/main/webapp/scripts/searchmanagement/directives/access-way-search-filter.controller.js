/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('AccessWaySearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout', 'CurrentProjectService',
    '$location',
    function($scope, VariableSearchService, $timeout, CurrentProjectService,
      $location) {
      // prevent access-way changed events during init
      var initializing = true;
      var selectionChanging = false;
      var cache = {
        searchText: null,
        filter: null,
        query: null,
        projectId: null,
        searchResult: null
      };
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
          $scope.currentSearchParams.filter['access-way'] = accessWay.key;
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
        var query = $location.search().query;
        if (searchText === cache.SearchText &&
          _.isEqual(cache.filter, cleanedFilter) &&
           cache.projectId === currentProjectId &&
           query === cache.query
          ) {
          return cache.searchResult;
        }
        return VariableSearchService.findAccessWays(
          searchText, cleanedFilter, currentProjectId, query)
          .then(function(accessWays) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.projectId = currentProjectId;
            cache.query = query;
            cache.searchResult = accessWays;
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
