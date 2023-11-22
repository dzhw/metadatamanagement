/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentSearchFilterController', [
    '$scope', 'InstrumentSearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$location', '$q',
    function($scope, InstrumentSearchService, $timeout,
      CurrentProjectService, $rootScope, $location, $q) {
      // prevent instrument changed events during init
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
          $scope.currentSearchParams.filter.instrument) {
          $rootScope.$broadcast('start-ignoring-404');
          InstrumentSearchService.findOneById(
            $scope.currentSearchParams.filter.instrument,
            ['id', 'masterId', 'description']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentInstrument = result;
              } else {
                $scope.currentInstrument = {
                  id: $scope.currentSearchParams.filter.instrument
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentInstrument = {
                  id: $scope.currentSearchParams.filter.instrument
                };
                $timeout(function() {
                  $scope.instrumentFilterForm.instrumentFilter.$setValidity(
                    'md-require-match', false);
                }, 500);
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
          $scope.currentSearchParams.filter.instrument = instrument.id;
        } else {
          delete $scope.currentSearchParams.filter.instrument;
        }
        $scope.instrumentChangedCallback();
      };

      $scope.searchInstruments = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
        CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'instrument');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }
        //Search Call to Elasticsearch
        return InstrumentSearchService.findInstrumentDescriptions(searchText,
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
      $scope.$watch('currentSearchParams.filter.instrument', function() {
        init();
      });
    }
  ]);
