/* global _, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'SearchFilterHelperService', '$timeout', 'StudyIdBuilderService',
    'CurrentProjectService', '$element', 'CleanJSObjectService', '$mdSelect',
    function($scope, SearchFilterHelperService, $timeout,
      StudyIdBuilderService, CurrentProjectService, $element,
      CleanJSObjectService, $mdSelect) {
      var elasticSearchTypeChanged = false;
      $scope.filtersCollapsed = false;

      var selectStudyForProject = function() {
        if (!_.includes($scope.availableFilters, 'study')) {
          return;
        }
        var currentProject = CurrentProjectService.getCurrentProject();
        if (currentProject) {
          if (!$scope.currentSearchParams.filter) {
            $scope.currentSearchParams.filter = {};
          }
          $scope.currentSearchParams.filter.study =
            StudyIdBuilderService.buildStudyId(currentProject.id);
          if (!_.includes($scope.selectedFilters, 'study')) {
            $scope.selectedFilters.push('study');
          }
        }
      };

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
        $scope.availableFilters = SearchFilterHelperService.getAvailableFilters(
          $scope.currentElasticsearchType);
        $scope.displayAvailableFilters = createDisplayAvailableFilterList(
          $scope.availableFilters);
        $scope.availableHiddenFilters = _.intersection(
          SearchFilterHelperService.getHiddenFilters(
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
        selectStudyForProject();
        $timeout(function() {
          elasticSearchTypeChanged = false;
        });
      });

      $scope.onOneFilterChanged = function() {
        $scope.filterChangedCallback();
      };

      $scope.$watch('selectedFilters', function(newSelectedFilters,
        oldSelectedFilters) {
        if (elasticSearchTypeChanged) {
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

      $scope.$on('current-project-changed', function() {
        var currentProject = CurrentProjectService.getCurrentProject();
        if (currentProject) {
          selectStudyForProject();
        } else {
          if (_.includes($scope.selectedFilters, 'study')) {
            _.remove($scope.selectedFilters, function(selectedFilter) {
              return selectedFilter === 'study';
            });
            delete $scope.currentSearchParams.filter.study;
          }
        }
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
    }
  ]);
