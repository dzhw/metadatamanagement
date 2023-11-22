/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchFilterController', [
    '$scope', 'DataSetSearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$location', '$q',
    function($scope, DataSetSearchService, $timeout,
      CurrentProjectService, $rootScope, $location, $q) {
      // prevent data-set changed events during init
      var initializing = true;
      var selectionChanging = false;
      var cache = {
        searchText: null,
        filter: null,
        type: null,
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
          $scope.currentSearchParams.filter['data-set']) {
          $rootScope.$broadcast('start-ignoring-404');
          DataSetSearchService.findOneById(
            $scope.currentSearchParams.filter['data-set'],
            ['id', 'masterId', 'description']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentDataSet = result;
              } else {
                $scope.currentDataSet = {
                  id: $scope.currentSearchParams.filter['data-set']
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentDataSet = {
                  id: $scope.currentSearchParams.filter['data-set']
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
          $scope.currentSearchParams.filter['data-set'] = dataSet.id;
        } else {
          delete $scope.currentSearchParams.filter['data-set'];
        }
        $scope.datasetChangedCallback();
      };

      $scope.searchDataSets = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
        CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'data-set');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }
        //Search Call to Elasticsearch
        return DataSetSearchService.findDataSetDescriptions(searchText,
           cleanedFilter, $scope.type, $scope.query, $scope.projectId)
          .then(function(descriptions) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = descriptions;
            cache.type = $scope.type;
            cache.query = $scope.query;
            cache.projectId = $scope.projectId;
            return descriptions;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter["data-set"]', function() {
        init();
      });
    }
  ]);
