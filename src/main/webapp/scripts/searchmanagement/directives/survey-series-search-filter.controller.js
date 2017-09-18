/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySeriesSearchFilterController', [
    '$scope', 'StudySearchService', '$timeout', 'CurrentProjectService',
    'LanguageService',
    function($scope, StudySearchService, $timeout,
        CurrentProjectService, LanguageService) {
      // prevent survey-series changed events during init
      var initializing = true;
      var selectionChanging = false;
      var lastSearchText;
      var lastFilterDe;
      var lastFilterEn;
      var lastProjectId;
      var lastSearchResult;
      var currentFilterByLanguage;
      var init = function() {
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['survey-series-de'] &&
          $scope.currentSearchParams.filter['survey-series-en']) {

          if (LanguageService.getCurrentInstantly() === 'de') {
            currentFilterByLanguage =
              $scope.currentSearchParams.filter['survey-series-de'];
          } else {
            currentFilterByLanguage =
              $scope.currentSearchParams.filter['survey-series-en'];
          }

          $scope.searchSurveySeries(currentFilterByLanguage)
            .then(function(surveySeries) {

                if (!$scope.currentSurveySeries) {
                  $scope.currentSurveySeries = {};
                }

                if (surveySeries.length === 1) {
                  $scope.currentSurveySeries = surveySeries[0];
                  return;
                } else if (surveySeries.length > 1) {
                  var index = _.indexOf(surveySeries, currentFilterByLanguage);
                  if (index > -1) {
                    $scope.currentSurveySeries = surveySeries[index];
                    return;
                  }
                }
                //survey series was not found
                $scope.currentSurveySeries.de =
                  $scope.currentSearchParams.filter['survey-series-de'];
                $scope.currentSurveySeries.en =
                    $scope.currentSearchParams.filter['survey-series-en'];
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
          $scope.currentSearchParams.filter['survey-series-de'] =
          surveySeries.de;
          $scope.currentSearchParams.filter['survey-series-en'] =
          surveySeries.en;
        } else {
          delete $scope.currentSearchParams.filter['survey-series-de'];
          delete $scope.currentSearchParams.filter['survey-series-en'];
        }
        $scope.surveySeriesChangedCallback();
      };

      $scope.searchSurveySeries = function(searchText) {
        var cleanedFilterDe = _.omit($scope.currentSearchParams.filter,
          'survey-series-de');
        var cleanedFilterEn = _.omit($scope.currentSearchParams.filter,
          'survey-series-en');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilterDe, cleanedFilterDe) &&
          _.isEqual(lastFilterEn, cleanedFilterEn) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return StudySearchService.findSurveySeries(cleanedFilterDe,
          cleanedFilterEn, currentProjectId)
          .then(function(surveySeries) {
            lastSearchText = searchText;
            lastFilterDe = _.cloneDeep(cleanedFilterDe);
            lastFilterEn = _.cloneDeep(cleanedFilterEn);
            lastProjectId = currentProjectId;
            lastSearchResult = surveySeries;
            return surveySeries;
          }
        );
      };
      $scope.$watch(['currentSearchParams.filter["survey-series-de"]',
        'currentSearchParams.filter["survey-series-en"]'],
        function() {
          init();
        });
    }
  ]);
