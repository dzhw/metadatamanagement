'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchFilterController', [
    '$scope', 'SearchDao', 'DataSetSearchService',
    function($scope, SearchDao, DataSetSearchService) {
      // prevent data-set changed events during init
      var initializing = true;
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
        return SearchDao.search(searchText, 1,
            undefined, $scope.currentSearchParams.filter,
            'data_sets',
            100).then(function(data) {
              return data.hits.hits;
            }
          );
      };
      init();
    }
  ]);
