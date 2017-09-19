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

      var mapI18nFilterArray = function(filter) {
        var i18nCleanedFilter = [];
        var i18nActualEnding = '-' + $scope.currentLanguage;
        var i18nAnotherEnding;
        if (i18nActualEnding === '-de') {
          i18nAnotherEnding = '-en';
        } else {
          i18nAnotherEnding = '-de';
        }
        var index;
        for (index = 0; index < filter.length; ++index) {
          //add i18n free filter name
          if (filter[index].endsWith(i18nActualEnding)) {
            i18nCleanedFilter.push(filter[index].slice(0, -3));
          } else if (filter[index].endsWith(i18nAnotherEnding)) {
            //do nothing, that removes the non actual ending
          } else {
            i18nCleanedFilter.push(filter[index]);
          }
        }
        return i18nCleanedFilter;
      };

      var mapI18nFilterObject = function(filter) {
        var i18nCleanedFilter = {};
        var i18nActualEnding = '-' + $scope.currentLanguage;
        var i18nAnotherEnding;
        if (i18nActualEnding === '-de') {
          i18nAnotherEnding = '-en';
        } else {
          i18nAnotherEnding = '-de';
        }

        for (var property in filter) {
          //add i18n free filter name
          if (property.endsWith(i18nActualEnding)) {
            i18nCleanedFilter[property.slice(0, -3)] = filter[property];
          } else if (property.endsWith(i18nAnotherEnding)) {
            //do nothing, that removes the another, non actual language ending
          } else {
            i18nCleanedFilter[property] = filter[property];
          }
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
          var selectedI18nFreeFilters =
            mapI18nFilterObject($scope.currentSearchParams.filter);

          //Check for I18nFilter
          var i18nFreeFilter =
            mapI18nFilterArray($scope.availableFilters);
          $scope.availableFilters = i18nFreeFilter;
          $scope.selectedFilters = _.intersection(
            _.keys(selectedI18nFreeFilters),
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
