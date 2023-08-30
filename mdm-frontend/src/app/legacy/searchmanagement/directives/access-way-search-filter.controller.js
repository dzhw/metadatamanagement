/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('AccessWaySearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout', 'CurrentProjectService',
    '$location', '$q', 'DataSetSearchService',
    function($scope, VariableSearchService, $timeout, CurrentProjectService,
      $location, $q, DataSetSearchService) {
      // prevent access-way changed events during init
      var initializing = true;
      var selectionChanging = false;
      var cache = {
        searchText: null,
        filter: null,
        query: null,
        type: null,
        projectId: null,
        searchResult: null
      };

      this.findAccessWays = VariableSearchService.findAccessWays;

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
        $scope.type = $location.search().type;
        if (searchText === cache.SearchText &&
          $scope.type === cache.type &&
          _.isEqual(cache.filter, cleanedFilter) &&
           cache.projectId === currentProjectId &&
           query === cache.query
          ) {
          return $q.resolve(cache.searchResult);
        }
        var findAccessWays = VariableSearchService.findAccessWays;
        if ($scope.type === 'data_sets') {
          findAccessWays = DataSetSearchService.findAccessWays;
        }
        return findAccessWays(
          searchText, cleanedFilter, currentProjectId, query)
          .then(function(accessWays) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.projectId = currentProjectId;
            cache.query = query;
            cache.type = $scope.type;
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
