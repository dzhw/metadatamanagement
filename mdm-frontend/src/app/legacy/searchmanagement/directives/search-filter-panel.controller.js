/* global _, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'SearchHelperService', '$timeout',
    '$element', 'CleanJSObjectService', '$mdSelect',
    function($scope, SearchHelperService, $timeout,
      $element, CleanJSObjectService, $mdSelect) {
      var elasticSearchTypeChanged = false;
      var searchParamsFilterChanged = false;
      $scope.filtersCollapsed = false;

      var createDisplayAvailableFilterList = function(availableFilters) {
        var displayAvailableFilters = [];
        var i18nGermanEnding = '-de';
        var i18nEnglishEnding = '-en';
        availableFilters.forEach(function(filter) {
          if (filter.endsWith(i18nGermanEnding)) {
            if ($scope.currentLanguage === 'de') {
              displayAvailableFilters.push(filter);
            }
            //Else case, no save of the german filter
          } else if (filter.endsWith(i18nEnglishEnding)) {
            if ($scope.currentLanguage === 'en') {
              displayAvailableFilters.push(filter);
            }
            //Else Case no save of the english filter
          } else {
            //Standard Case
            displayAvailableFilters.push(filter);
          }
        });

        return displayAvailableFilters;
      };

      $scope.$watch('currentElasticsearchType', function() {
        elasticSearchTypeChanged = true;
        $scope.availableFilters = SearchHelperService.getAvailableFilters(
          $scope.currentElasticsearchType);
        $scope.displayAvailableFilters = createDisplayAvailableFilterList(
          $scope.availableFilters);
        $scope.availableHiddenFilters = _.intersection(
          SearchHelperService.getHiddenFilters(
            $scope.currentElasticsearchType),
            _.keys($scope.currentSearchParams.filter)
        );
        $scope.selectedFilters = [];
        if ($scope.currentSearchParams.filter) {
          $scope.selectedFilters = _.intersection(
            _.keys($scope.currentSearchParams.filter),
            _.union($scope.availableFilters, $scope.availableHiddenFilters)
          );
        }
        $timeout(function() {
          elasticSearchTypeChanged = false;
        });
      });

      $scope.$on('user-logged-out', function() {
        $scope.selectedFilters = [];
      });

      $scope.onOneFilterChanged = function() {
        $scope.filterChangedCallback();
      };

      $scope.$watch('selectedFilters', function(newSelectedFilters,
        oldSelectedFilters) {
        $scope.filtersCollapsed = false;
        if ($scope.selectedFilters && !_.isEmpty($scope.selectedFilters)) {
          $timeout(function() {
            // add md class manually to fix overlapping labels
            $element.find('.fdz-filter-select').addClass('md-input-has-value');
          });
        }
        if (elasticSearchTypeChanged || searchParamsFilterChanged) {
          return;
        }
        var unselectedFilters = _.difference(
          oldSelectedFilters, newSelectedFilters);
        if ($scope.currentSearchParams.filter && unselectedFilters.length > 0) {
          unselectedFilters.forEach(function(unselectedFilter) {
            delete $scope.currentSearchParams.filter[unselectedFilter];
          });
          $scope.availableHiddenFilters = _.difference(
            $scope.availableHiddenFilters, unselectedFilters);
          $scope.filterChangedCallback();
        }
      });

      $scope.$watch('currentSearchParams.filter', function() {
        searchParamsFilterChanged = true;
        $scope.selectedFilters = [];
        if ($scope.currentSearchParams.filter) {
          $scope.selectedFilters = _.intersection(
            _.keys($scope.currentSearchParams.filter),
            _.union($scope.availableFilters, $scope.availableHiddenFilters)
          );
        }
        $timeout(function() {
          searchParamsFilterChanged = false;
        });
      });

      $element.find('#searchFilterInput').on('keydown', function(event) {
          // close filter chooser on escape
          if (event.keyCode === 27 &&
            CleanJSObjectService.isNullOrEmpty($scope.filterSearchTerm)) {
            return;
          }
          // The md-select directive eats keydown events for some quick select
          // logic. Since we have a search input here, we don't need that logic.
          event.stopPropagation();
        });

      $scope.closeSelectMenu = function() {
        $mdSelect.hide();
      };

      $scope.clearFilterSearchTerm = function() {
        $scope.filterSearchTerm = '';
      };

      $scope.hideMobileKeyboard = function() {
        document.activeElement.blur();
      };

      $scope.getFilterPanelStyle = function() {
        var minHeight = (56 * $scope.selectedFilters.length) + 'px';
        return {'min-height': minHeight};
      };
    }
  ]);
