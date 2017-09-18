/* global _, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SearchFilterPanelController', [
    '$scope', 'SearchFilterHelperService', '$timeout', 'StudyIdBuilderService',
    'CurrentProjectService', '$element', 'CleanJSObjectService', '$mdSelect',
    'LanguageService',
    function($scope, SearchFilterHelperService, $timeout,
      StudyIdBuilderService, CurrentProjectService, $element,
      CleanJSObjectService, $mdSelect, LanguageService) {
      var elasticSearchTypeChanged = false;
      $scope.filtersCollapsed = false;

      var checkForI18nFilter = function(filter) {
        var i18nCleanedFilter = {};
        var i18nFilterEnding = '-' + LanguageService.getCurrentInstantly();
        for (var property in filter) {
          //add i18n free filter name
          if (filter.hasOwnProperty(property) &&
            property.endsWith(i18nFilterEnding)) {
            i18nCleanedFilter[property.slice(0, -3)] = filter[property];
          }
          i18nCleanedFilter[property] = filter[property];
        }
        return i18nCleanedFilter;
      };

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

      $scope.$watch('currentElasticsearchType', function() {
        elasticSearchTypeChanged = true;
        $scope.availableFilters = SearchFilterHelperService.getAvailableFilters(
          $scope.currentElasticsearchType);
        $scope.availableHiddenFilters = _.intersection(
          SearchFilterHelperService.getHiddenFilters(
            $scope.currentElasticsearchType),
            _.keys($scope.currentSearchParams.filter)
        );
        $scope.selectedFilters = [];
        if ($scope.currentSearchParams.filter) {

          //Check for I18nFilter
          var i18nFilter =
            checkForI18nFilter($scope.currentSearchParams.filter);
          if (i18nFilter) {
            $scope.selectedFilters = _.intersection(
              _.keys(i18nFilter),
              _.union($scope.availableFilters, $scope.availableHiddenFilters)
            );
          } else {
            $scope.selectedFilters = _.intersection(
              _.keys($scope.currentSearchParams.filter),
              _.union($scope.availableFilters, $scope.availableHiddenFilters)
            );
          }
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
