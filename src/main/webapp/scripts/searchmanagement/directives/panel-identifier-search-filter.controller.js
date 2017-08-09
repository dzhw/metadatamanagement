/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('PanelIdentifierSearchFilterController', [
    '$scope', 'VariableSearchService', '$timeout',
    function($scope, VariableSearchService, $timeout) {
      // prevent panel-identifier changed events during init
      var initializing = true;
      var lastSearchText;
      var lastFilter;
      var lastSearchResult;
      var init = function() {
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
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (panelIdentifier) {
          $scope.currentSearchParams.filter['panel-identifier'] =
          panelIdentifier;
        } else {
          delete $scope.currentSearchParams.filter['panel-identifier'];
        }
        if (!initializing) {
          $scope.panelIdentifierChangedCallback();
        }
        initializing = false;
      };

      $scope.searchPanelIdentifiers = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'panel-identifier');
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter)) {
          return lastSearchResult;
        }
        return VariableSearchService.findPanelIdentifiers(
          searchText, cleanedFilter).then(function(panelIdentifiers) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastSearchResult = panelIdentifiers;
            return panelIdentifiers;
          }
        );
      };
      init();
    }
  ]);
