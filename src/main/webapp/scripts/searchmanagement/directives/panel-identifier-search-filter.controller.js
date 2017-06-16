/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('PanelIdentifierSearchFilterController', [
    '$scope', 'VariableSearchService',
    function($scope, VariableSearchService) {
      // prevent panel-identifier changed events during init
      var initializing = true;
      var init = function() {
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['panel-identifier']) {
          $scope.currentPanelIdentifier =
          $scope.currentSearchParams.filter['panel-identifier'];
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
        return VariableSearchService.findPanelIdentifiers(
          searchText, _.omit($scope.currentSearchParams.filter,
            'panel-identifier'));
      };
      init();
    }
  ]);
