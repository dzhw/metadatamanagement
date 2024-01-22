/* global _, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'SearchHelperService', '$timeout',
    '$element', 'CleanJSObjectService', '$mdSelect', 'Principal',
    function($scope, SearchHelperService, $timeout,
      $element, CleanJSObjectService, $mdSelect, Principal) {

      /* filters that need to be removed because they sould only be visible
      in the sidenav for public users */
      var irrelevantFiltersMapping = {
        'related_publications': ['year', 'language']
      };
      var elasticSearchTypeChanged = false;
      var searchParamsFilterChanged = false;
      $scope.filtersCollapsed = false;
      $scope.transmissionViaVerbundFdb = false;
      $scope.externalDataPackage = false;

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
        
        // show filter only to users with role publisher
        if (!Principal.isPublisher()) {
          displayAvailableFilters = displayAvailableFilters.filter(item => item !== "externalDataPackage");
          displayAvailableFilters = displayAvailableFilters.filter(item => item !== "transmissionViaVerbundFdb");
          displayAvailableFilters = displayAvailableFilters.filter(item => item !== "approved-usage");
          displayAvailableFilters = displayAvailableFilters.filter(item => item !== "approved-usage-list");
        }
        
        return displayAvailableFilters;
      };

      $scope.$watch('currentElasticsearchType', function() {
        elasticSearchTypeChanged = true;
        $scope.availableFilters = SearchHelperService.getAvailableFilters(
          $scope.currentElasticsearchType); // currentElasticsearchType --> e.g. "data_packages"
        /* as this is the search panel for logged in users we need to remove some
        filters that sould only be shown in the sidenav for public users */
        removeIrrelevantFilter();
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
        $scope.transmissionViaVerbundFDB = $scope.currentSearchParams.filter['transmissionViaVerbundFdb'];
        $scope.externalDataPackage = $scope.currentSearchParams.filter['externalDataPackage'];
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

      /**
       * Method to remove all selected Filters.
       * It also removes the "useAndLogicApprovedUsage"-filter
       * which is a special filter for the list of all approved usages.
       */
      $scope.removeFilters = function() {
        $scope.selectedFilters = [];
        delete $scope.currentSearchParams.filter.useAndLogicApprovedUsage;
      }

      /**
       * Function to remove the filters that are irrelevant for the search panel.
       */
      var removeIrrelevantFilter = function() {
        var irrelevantFilters = irrelevantFiltersMapping[
          $scope.currentElasticsearchType];
        if (irrelevantFilters) {
          for (var i = 0; i < irrelevantFilters.length; i++) {
            var filter = irrelevantFilters[i];
            var filterIndex = $scope.availableFilters.indexOf(filter);
            if (filterIndex > -1) {
              $scope.availableFilters.splice(filterIndex, 1);
            }
          }
        }
      };

      $scope.isPublisher = function() {
        return Principal.isPublisher();
      };

      /**
       * Function to set the value of transmissionViaVerbundFDB in the filter of currentSearchParams.
       */
       $scope.onTransmissionViaVerbundFdbClick = function() {
        $scope.transmissionViaVerbundFdb = !$scope.transmissionViaVerbundFdb
        if ($scope.transmissionViaVerbundFdb) {
          $scope.currentSearchParams.filter['transmissionViaVerbundFdb'] = true;
        } else {
          $scope.currentSearchParams.filter['transmissionViaVerbundFdb'] = false;
        }
        $scope.filterChangedCallback();
      }

      /**
       * Function to set the value of externalDataPackage in the filter of currentSearchParams.
       */
      $scope.onExternalDataPackageClick = function() {
        $scope.externalDataPackage = !$scope.externalDataPackage
        if ($scope.externalDataPackage) {
          $scope.currentSearchParams.filter['externalDataPackage'] = true;
        } else {
          $scope.currentSearchParams.filter['externalDataPackage'] = false;
        }
        $scope.filterChangedCallback();
      }
    }
  ]);
