/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableSearchFilterController', [
    '$scope', 'SearchDao', 'VariableSearchService', '$timeout',
    'CurrentProjectService',
    function($scope, SearchDao, VariableSearchService, $timeout,
      CurrentProjectService) {
      // prevent variable changed events during init
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
          $scope.currentSearchParams.filter.variable) {
          VariableSearchService.findOneById(
            $scope.currentSearchParams.filter.variable).promise
            .then(function(result) {
              if (result) {
                $scope.currentVariable = {_source: result};
              } else {
                $scope.currentVariable = {
                  _source: {
                    id: $scope.currentSearchParams.filter.variable
                  }
                };
              }
            }, function() {
                $scope.currentVariable = {
                  _source: {
                    id: $scope.currentSearchParams.filter.variable
                  }
                };
                $timeout(function() {
                  $scope.variableFilterForm.variableFilter.$setValidity(
                    'md-require-match', false);
                }, 500);
                $scope.variableFilterForm.variableFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(variable) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (variable) {
          $scope.currentSearchParams.filter.variable = variable._source.id;
        } else {
          delete $scope.currentSearchParams.filter.variable;
        }
        $scope.variableChangedCallback();
      };

      $scope.searchVariables = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'variable');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
            CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            currentProjectId, cleanedFilter,
            'variables',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastProjectId = currentProjectId;
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      $scope.$watch('currentSearchParams.filter.variable', function() {
        init();
      });
    }
  ]);
