/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableSearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$q', '$location',
    function($scope, VariableSearchService, $timeout,
      CurrentProjectService, $rootScope, $q, $location) {
      // prevent variable changed events during init
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
          $scope.currentSearchParams.filter.variable) {
          $rootScope.$broadcast('start-ignoring-404');
          VariableSearchService.findOneById(
            $scope.currentSearchParams.filter.variable,
            ['id','masterId','label']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentVariable = result;
              } else {
                $scope.currentVariable = {
                  id: $scope.currentSearchParams.filter.variable
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentVariable = {
                  id: $scope.currentSearchParams.filter.variable
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
          $scope.currentSearchParams.filter.variable = variable.id;
        } else {
          delete $scope.currentSearchParams.filter.variable;
        }
        $scope.variableChangedCallback();
      };

      $scope.searchVariables = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'variable');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return VariableSearchService.findVariableLabels(searchText,
           cleanedFilter, $scope.type, $scope.query, $scope.projectId)
          .then(function(variableLabels) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = variableLabels;
            cache.type = $scope.type;
            cache.query = $scope.query;
            cache.projectId = $scope.projectId;
            return variableLabels;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter.variable', function() {
        init();
      });
    }
  ]);
