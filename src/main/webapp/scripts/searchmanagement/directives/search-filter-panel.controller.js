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

      var mapI18nFilter = function(filter) {
        var i18nCleanedFilter = [];
        var i18nDeEnding = '-de';
        var i18nEnEnding = '-en';
        var index;
        for (index = 0; index < filter.length; ++index) {
          //add i18n free filter name
          if (filter[index].endsWith(i18nDeEnding)) {
            i18nCleanedFilter.push(filter[index].slice(0, -3));
          } else if (filter[index].endsWith(i18nEnEnding)) {
            //do nothing, that removes the en ending
          } else {
            i18nCleanedFilter.push(filter[index]);
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

          //Check for I18nFilter
          var i18nFreeFilter =
            mapI18nFilter($scope.availableFilters);
          $scope.availableFilters = i18nFreeFilter;
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
