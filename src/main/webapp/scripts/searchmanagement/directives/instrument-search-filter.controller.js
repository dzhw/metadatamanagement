/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentSearchFilterController', [
    '$scope', 'SearchDao', 'InstrumentSearchService', '$timeout',
    'CurrentProjectService',
    function($scope, SearchDao, InstrumentSearchService, $timeout,
      CurrentProjectService) {
      // prevent instrument changed events during init
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
          $scope.currentSearchParams.filter.instrument) {
          InstrumentSearchService.findOneById(
            $scope.currentSearchParams.filter.instrument).promise
            .then(function(result) {
              if (result) {
                $scope.currentInstrument = {_source: result};
              } else {
                $scope.currentInstrument = {
                  _source: {
                    id: $scope.currentSearchParams.filter.instrument
                  }
                };
              }
            }, function() {
                $scope.currentInstrument = {
                  _source: {
                    id: $scope.currentSearchParams.filter.instrument
                  }
                };
                $timeout(function() {
                  $scope.instrumentFilterForm.instrumentFilter.$setValidity(
                    'md-require-match', false);
                });
                $scope.instrumentFilterForm.instrumentFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(instrument) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (instrument) {
          $scope.currentSearchParams.filter.instrument = instrument._source.id;
        } else {
          delete $scope.currentSearchParams.filter.instrument;
        }
        $scope.instrumentChangedCallback();
      };

      $scope.searchInstruments = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'instrument');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
            CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            currentProjectId, cleanedFilter,
            'instruments',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastProjectId = currentProjectId;
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      $scope.$watch('currentSearchParams.filter.instrument', function() {
        init();
      });
    }
  ]);
