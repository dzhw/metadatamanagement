/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('AccessWaySearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout',
    function($scope, VariableSearchService, $timeout) {
      // prevent access-way changed events during init
      var initializing = true;
      var lastSearchText;
      var lastFilter;
      var lastSearchResult;
      var init = function() {
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
                  });
                  $scope.accessWayFilterForm.accessWayFilter.$setTouched();
                }
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(accessWay) {
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (accessWay) {
          $scope.currentSearchParams.filter['access-way'] = accessWay;
        } else {
          delete $scope.currentSearchParams.filter['access-way'];
        }
        if (!initializing) {
          $scope.accessWayChangedCallback();
        }
        initializing = false;
      };

      $scope.searchAccessWays = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'access-way');
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter)) {
          return lastSearchResult;
        }
        return VariableSearchService.findAccessWays(
          searchText, cleanedFilter).then(function(accessWays) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastSearchResult = accessWays;
            return accessWays;
          }
        );
      };
      init();
    }
  ]);
