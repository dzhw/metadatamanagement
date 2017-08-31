/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DerivedVariablesIdentifierSearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout', 'CurrentProjectService',
    function($scope, VariableSearchService, $timeout, CurrentProjectService) {
      // prevent derived-variables-identifier changed events during init
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
          $scope.currentSearchParams.filter['derived-variables-identifier']) {
          $scope.searchDerivedVariablesIdentifiers(
            $scope.currentSearchParams.filter['derived-variables-identifier'])
            .then(
              function(derivedVariablesIdentifiers) {
                if (derivedVariablesIdentifiers.length === 1) {
                  $scope.currentDerivedVariablesIdentifier =
                    derivedVariablesIdentifiers[0];
                  return;
                } else if (derivedVariablesIdentifiers.length > 1) {
                  var index = _.indexOf(derivedVariablesIdentifiers,
                    $scope.currentSearchParams
                      .filter['derived-variables-identifier']);
                  if (index > -1) {
                    $scope.currentDerivedVariablesIdentifier =
                      derivedVariablesIdentifiers[index];
                    return;
                  }
                }
                $scope.currentDerivedVariablesIdentifier =
                  $scope.currentSearchParams
                    .filter['derived-variables-identifier'];
                $timeout(function() {
                  $scope.derivedVariablesIdentifierFilterForm
                    .derivedVariablesIdentifierFilter
                    .$setValidity('md-require-match', false);
                }, 500);
                $scope.derivedVariablesIdentifierFilterForm
                  .derivedVariablesIdentifierFilter
                  .$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(derivedVariablesIdentifier) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (derivedVariablesIdentifier) {
          $scope.currentSearchParams.filter['derived-variables-identifier'] =
          derivedVariablesIdentifier;
        } else {
          delete $scope.currentSearchParams
            .filter['derived-variables-identifier'];
        }
        $scope.derivedVariablesIdentifierChangedCallback();
      };

      $scope.searchDerivedVariablesIdentifiers = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'derived-variables-identifier');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return VariableSearchService.findDerivedVariablesIdentifiers(
          searchText, cleanedFilter,
          currentProjectId)
          .then(function(derivedVariablesIdentifiers) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastProjectId = currentProjectId;
            lastSearchResult = derivedVariablesIdentifiers;
            return derivedVariablesIdentifiers;
          }
        );
      };
      $scope
        .$watch('currentSearchParams.filter["derived-variables-identifier"]',
        function() {
          init();
        });
    }
  ]);
