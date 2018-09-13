/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('PanelIdentifierSearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout', 'CurrentProjectService',
    '$location', '$q',
    function($scope, VariableSearchService, $timeout, CurrentProjectService,
      $location, $q) {
      // prevent panel-identifier changed events during init
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
          $scope.currentSearchParams.filter['panel-identifier']) {
          $scope.searchPanelIdentifiers(
            $scope.currentSearchParams.filter['panel-identifier']).then(
              function(panelIdentifiers) {
                if (panelIdentifiers.length === 1) {
                  $scope.currentPanelIdentifier = panelIdentifiers[0];
                  return;
                } else if (panelIdentifiers.length > 1) {
                  var index = _.indexOf(panelIdentifiers,
                    $scope.currentSearchParams.filter['panel-identifier']);
                  if (index > -1) {
                    $scope.currentPanelIdentifier = panelIdentifiers[index];
                    return;
                  }
                }
                //panel identifier was not found
                $scope.currentPanelIdentifier =
                  $scope.currentSearchParams.filter['panel-identifier'];
                $timeout(function() {
                  $scope.panelIdentifierFilterForm.panelIdentifierFilter
                    .$setValidity('md-require-match', false);
                }, 500);
                $scope.panelIdentifierFilterForm.panelIdentifierFilter
                  .$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(panelIdentifier) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (panelIdentifier) {
          $scope.currentSearchParams.filter['panel-identifier'] =
          panelIdentifier.key;
        } else {
          delete $scope.currentSearchParams.filter['panel-identifier'];
        }
        $scope.panelIdentifierChangedCallback();
      };

      $scope.searchPanelIdentifiers = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'panel-identifier');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var query = $location.search().query || null;
        if (searchText === cache.searchText &&
          _.isEqual(cache.filter, cleanedFilter) &&
          cache.projectId === currentProjectId &&
          cache.query === query) {
          return $q.resolve(cache.searchResult);
        }
        return VariableSearchService.findPanelIdentifiers(
          searchText, cleanedFilter,
          currentProjectId, query)
          .then(function(panelIdentifiers) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.projectId = currentProjectId;
            cache.query = query;
            cache.searchResult = panelIdentifiers;
            return panelIdentifiers;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter["panel-identifier"]',
        function() {
          init();
        });
    }
  ]);
