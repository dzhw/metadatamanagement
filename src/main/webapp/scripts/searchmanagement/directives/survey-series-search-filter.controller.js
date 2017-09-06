/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySeriesSearchFilterController', [
    '$scope', 'StudySearchService', '$timeout', 'CurrentProjectService',
    function($scope, StudySearchService, $timeout,
        CurrentProjectService) {
      // prevent survey-series changed events during init
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
          $scope.currentSearchParams.filter['survey-series']) {
          $scope.searchSurveySeries(
            $scope.currentSearchParams.filter['survey-series']).then(
              function(surveySeries) {
                if (surveySeries.length === 1) {
                  $scope.currentSurveySeries = surveySeries[0];
                  return;
                } else if (surveySeries.length > 1) {
                  var index = _.indexOf(surveySeries,
                    $scope.currentSearchParams.filter['survey-series']);
                  if (index > -1) {
                    $scope.currentSurveySeries = surveySeries[index];
                    return;
                  }
                }
                //survey series was not found
                $scope.currentSurveySeries =
                  $scope.currentSearchParams.filter['survey-series'];
                $timeout(function() {
                  $scope.surveySeriesFilterForm.surveySeriesFilter
                    .$setValidity('md-require-match', false);
                }, 500);
                $scope.surveySeriesFilterForm.surveySeriesFilter
                  .$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(surveySeries) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (surveySeries) {
          $scope.currentSearchParams.filter['survey-series'] =
          surveySeries;
        } else {
          delete $scope.currentSearchParams.filter['survey-series'];
        }
        $scope.surveySeriesChangedCallback();
      };

      $scope.searchSurveySeries = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'survey-series');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return StudySearchService.findSurveySeries(
          searchText, cleanedFilter,
          currentProjectId)
          .then(function(surveySeries) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastProjectId = currentProjectId;
            lastSearchResult = surveySeries;
            return surveySeries;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter["survey-series"]',
        function() {
          init();
        });
    }
  ]);
