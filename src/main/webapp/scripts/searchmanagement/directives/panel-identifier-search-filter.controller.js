/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('PanelIdentifierSearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout', 'CurrentProjectService',
    function($scope, VariableSearchService, $timeout, CurrentProjectService) {
      // prevent panel-identifier changed events during init
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
          $scope.currentSearchParams.filter['panel-identifier']) {
          $scope.searchPanelIdentifiers(
            $scope.currentSearchParams.filter['panel-identifier']).then(
              function(panelIdentifiers) {
                if (panelIdentifiers.length === 1) {
                  $scope.currentPanelIdentifier = panelIdentifiers[0];
                } else {
                  $scope.currentPanelIdentifier =
                    $scope.currentSearchParams.filter['panel-identifier'];
                  $timeout(function() {
                    $scope.panelIdentifierFilterForm.panelIdentifierFilter
                      .$setValidity('md-require-match', false);
                  });
                  $scope.panelIdentifierFilterForm.panelIdentifierFilter
                    .$setTouched();
                }
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
          panelIdentifier;
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
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return VariableSearchService.findPanelIdentifiers(
          searchText, cleanedFilter,
          currentProjectId)
          .then(function(panelIdentifiers) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastProjectId = currentProjectId;
            lastSearchResult = panelIdentifiers;
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
