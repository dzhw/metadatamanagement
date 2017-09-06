/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchFilterController', [
    '$scope', 'SearchDao', 'DataSetSearchService', '$timeout',
    'CurrentProjectService',
    function($scope, SearchDao, DataSetSearchService, $timeout,
      CurrentProjectService) {
      // prevent data-set changed events during init
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
                }, 500);
                $scope.dataSetFilterForm.dataSetFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(dataSet) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (dataSet) {
          $scope.currentSearchParams.filter['data-set'] = dataSet._source.id;
        } else {
          delete $scope.currentSearchParams.filter['data-set'];
        }
        $scope.datasetChangedCallback();
      };

      $scope.searchDataSets = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'data-set');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            currentProjectId, cleanedFilter,
            'data_sets',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastProjectId = currentProjectId;
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      $scope.$watch('currentSearchParams.filter["data-set"]', function() {
        init();
      });
    }
  ]);
