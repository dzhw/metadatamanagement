/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('RepeatedMeasurementIdentifierSearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout', 'CurrentProjectService',
    '$location', '$q',
    function($scope, VariableSearchService, $timeout, CurrentProjectService,
      $location, $q) {
      // prevent repeated-measurement-identifier changed events during init
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
          $scope.currentSearchParams.filter[
            'repeated-measurement-identifier']) {
          $scope.searchRepeatedMeasurementIdentifiers(
            $scope.currentSearchParams.filter[
              'repeated-measurement-identifier']).then(
              function(repeatedMeasurementIdentifiers) {
                if (repeatedMeasurementIdentifiers.length === 1) {
                  $scope.currentRepeatedMeasurementIdentifier =
                    repeatedMeasurementIdentifiers[0];
                  return;
                } else if (repeatedMeasurementIdentifiers.length > 1) {
                  var index = _.indexOf(repeatedMeasurementIdentifiers,
                    $scope.currentSearchParams.filter[
                      'repeated-measurement-identifier']);
                  if (index > -1) {
                    $scope.currentRepeatedMeasurementIdentifier =
                      repeatedMeasurementIdentifiers[index];
                    return;
                  }
                }
                //panel identifier was not found
                $scope.currentRepeatedMeasurementIdentifier =
                  $scope.currentSearchParams.filter[
                    'repeated-measurement-identifier'];
                $timeout(function() {
                  $scope.repeatedMeasurementIdentifierFilterForm
                    .repeatedMeasurementIdentifierFilter
                    .$setValidity('md-require-match', false);
                }, 500);
                $scope.repeatedMeasurementIdentifierFilterForm
                  .repeatedMeasurementIdentifierFilter
                  .$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(repeatedMeasurementIdentifier) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (repeatedMeasurementIdentifier) {
          $scope.currentSearchParams.filter['repeated-measurement-identifier'] =
          repeatedMeasurementIdentifier.key;
        } else {
          delete $scope.currentSearchParams.filter[
            'repeated-measurement-identifier'];
        }
        $scope.repeatedMeasurementIdentifierChangedCallback();
      };

      $scope.searchRepeatedMeasurementIdentifiers = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'repeated-measurement-identifier');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var query = $location.search().query || null;
        if (searchText === cache.searchText &&
          _.isEqual(cache.filter, cleanedFilter) &&
          cache.projectId === currentProjectId &&
          cache.query === query) {
          return $q.resolve(cache.searchResult);
        }
        return VariableSearchService.findRepeatedMeasurementIdentifiers(
          searchText, cleanedFilter,
          currentProjectId, query)
          .then(function(repeatedMeasurementIdentifiers) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.projectId = currentProjectId;
            cache.query = query;
            cache.searchResult = repeatedMeasurementIdentifiers;
            return repeatedMeasurementIdentifiers;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter[' +
        '"repeated-measurement-identifier"]',
        function() {
          init();
        });
    }
  ]);
