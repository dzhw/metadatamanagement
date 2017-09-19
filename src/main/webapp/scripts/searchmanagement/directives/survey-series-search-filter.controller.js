/* global _ */
/* @Author: Daniel Katzberg */
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
      var currentFilterByLanguage;

      //Search Method for Survey Series, call Elasticsearch
      $scope.searchSurveySeries = function(searchText) {
        var cleanedFilter = _.pick($scope.currentSearchParams.filter,
          'survey-series-' + $scope.currentLanguage);

        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }

        //Search Call to Elasticsearch
        return StudySearchService.findSurveySeries(searchText, '',
        cleanedFilter, currentProjectId)
          .then(function(surveySeries) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastProjectId = currentProjectId;
            lastSearchResult = surveySeries;
            return surveySeries;
          }
        );
      };

      //Init the de and en filter of survey series
      var init = function(currentLanguage) {

        //Just a change? No Init!
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }

        //Init the Filter
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['survey-series-' +
            currentLanguage]) {

          //Check with of both filter are active, dependings on acutual language
          currentFilterByLanguage =
            $scope.currentSearchParams.filter['survey-series-' +
              currentLanguage];

          //Search Survey Series and for Validation
          $scope.searchSurveySeries(currentFilterByLanguage)
            .then(function(surveySeries) {

                //Just one Survey Series exists
                if (surveySeries.length === 1) {
                  $scope.currentSurveySeries = surveySeries[0];
                  return;
                } else if (surveySeries.length > 1) {
                  //Standard Case, there are many Survey Series
                  surveySeries.forEach(function(surveySerie) {
                    if (surveySerie[currentLanguage] ===
                        currentFilterByLanguage) {
                      $scope.currentSurveySeries = surveySerie;
                      return;
                    }
                  });
                }

                //Survey Series was not found, set the last one
                if (!$scope.currentSurveySeries) {
                  $scope.currentSurveySeries =
                    $scope.currentSearchParams.filter['survey-series-' +
                    currentLanguage];
                  $timeout(function() {
                    $scope.surveySeriesFilterForm.surveySeriesFilter
                      .$setValidity('md-require-match', false);
                  }, 500);
                  $scope.surveySeriesFilterForm.surveySeriesFilter
                    .$setTouched();
                }
              });
        } else {
          initializing = false;
        }
      };

      //Set the Filter, if the user changed the Survey Series Filter
      $scope.onSelectionChanged = function(surveySeries) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }

        //Set the Survey Series Filter in the URL
        if (surveySeries) {
          $scope.currentSearchParams.filter['survey-series-' +
            $scope.currentLanguage] = surveySeries.de;
        } else {
          //No Survey Series is chosen, delete the Parameter in the URL
          delete $scope.currentSearchParams.filter['survey-series-' +
            $scope.currentLanguage];
        }
        $scope.surveySeriesChangedCallback();
      };

      //Initialize and watch the both Survey Series Filter
      $scope.$watch('currentSearchParams.filter["survey-series-' +
        $scope.currentLanguage + '"]',
        function() {
          init($scope.currentLanguage);
        });
    }
  ]);
