/* global _ */
/* @Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySeriesSearchFilterController', [
    '$scope', 'StudySearchService', '$timeout',
    function($scope, StudySearchService, $timeout) {
      // prevent survey-series changed events during init
      var initializing = true;
      var selectionChanging = false;
      var lastSearchText;
      var lastFilter;
      var lastSearchResult;
      var currentFilterByLanguage;

      //Search Method for Survey Series, call Elasticsearch
      $scope.searchSurveySeries = function(searchText, language) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'survey-series-' + language);

        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter)) {
          return lastSearchResult;
        }

        //Search Call to Elasticsearch
        return StudySearchService.findSurveySeries(searchText, cleanedFilter,
          language)
          .then(function(surveySeries) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
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
          $scope.searchSurveySeries(currentFilterByLanguage, currentLanguage)
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

                //Survey Series was not found check the language
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
          var i18nActualEnding = $scope.currentLanguage;
          var i18nAnotherEnding;
          if (i18nActualEnding === 'de') {
            i18nAnotherEnding = 'en';
          } else {
            i18nAnotherEnding = 'de';
          }

          if ($scope.currentSearchParams.filter['survey-series-' +
            i18nAnotherEnding]) {
            $scope.searchSurveySeries(
              $scope.currentSearchParams.filter['survey-series-' +
              i18nAnotherEnding], i18nAnotherEnding)
              .then(function(surveySeries) {
                if (surveySeries) {
                  $scope.currentSurveySeries = surveySeries[0];

                  $scope.currentSearchParams.filter['survey-series-' +
                    i18nActualEnding] = surveySeries[0][i18nActualEnding];
                  $scope.selectedFilters.push('survey-series-' +
                    i18nActualEnding);
                  delete $scope.selectedFilters[_.indexOf(
                    $scope.selectedFilters,
                    'survey-series-' + i18nAnotherEnding)
                    ];
                  delete $scope.currentSearchParams.filter['survey-series-' +
                    i18nAnotherEnding];
                  return;
                }
              });
          }
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
            $scope.currentLanguage] = surveySeries[$scope.currentLanguage];
        } else {
          //No Survey Series is chosen, delete the Parameter in the URL
          delete $scope.currentSearchParams.filter['survey-series-' +
            $scope.currentLanguage];
        }
        $scope.surveySeriesChangedCallback();
      };

      //Initialize and watch the current Survey Series Filter
      $scope.$watch('currentSearchParams.filter["survey-series-' +
        $scope.currentLanguage + '"]',
        function() {
          init($scope.currentLanguage);
        });
    }
  ]);
