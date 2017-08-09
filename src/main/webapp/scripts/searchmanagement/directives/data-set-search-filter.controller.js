/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchFilterController', [
    '$scope', 'SearchDao', 'DataSetSearchService', '$timeout',
    function($scope, SearchDao, DataSetSearchService, $timeout) {
      // prevent data-set changed events during init
      var initializing = true;
      var lastSearchText;
      var lastFilter;
      var lastSearchResult;
      var init = function() {
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['data-set']) {
          DataSetSearchService.findOneById(
            $scope.currentSearchParams.filter['data-set']).promise
            .then(function(result) {
              if (result) {
                $scope.currentDataSet = {_source: result};
              } else {
                $scope.currentDataSet = {
                  _source: {
                    id: $scope.currentSearchParams.filter['data-set']
                  }
                };
              }
            }, function() {
                $scope.currentDataSet = {
                  _source: {
                    id: $scope.currentSearchParams.filter['data-set']
                  }
                };
                $timeout(function() {
                  $scope.dataSetFilterForm.dataSetFilter.$setValidity(
                    'md-require-match', false);
                });
                $scope.dataSetFilterForm.dataSetFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(dataSet) {
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (dataSet) {
          $scope.currentSearchParams.filter['data-set'] = dataSet._source.id;
        } else {
          delete $scope.currentSearchParams.filter['data-set'];
        }
        if (!initializing) {
          $scope.datasetChangedCallback();
        }
        initializing = false;
      };

      $scope.searchDataSets = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'data-set');
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter)) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            undefined, cleanedFilter,
            'data_sets',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      init();
    }
  ]);
