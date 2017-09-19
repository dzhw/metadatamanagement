/* global _ */
/* @Author: Daniel Katzberg */
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

      //Search Method for Survey Series, call Elasticsearch
      $scope.searchSurveySeries = function(searchText) {
        var cleanedFilterDe = _.pick($scope.currentSearchParams.filter,
          'survey-series-de');
        var cleanedFilterEn = _.pick($scope.currentSearchParams.filter,
          'survey-series-en');
        var cleanedFilter;

        if (LanguageService.getCurrentInstantly() === 'de') {
          cleanedFilter = cleanedFilterDe;
        } else {
          cleanedFilter = cleanedFilterEn;
        }

        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilterDe, cleanedFilterDe) &&
          _.isEqual(lastFilterEn, cleanedFilterEn) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }

        //Search Call to Elasticsearch
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

      //Init the de and en filter of survey series
      var init = function() {

        //Just a change? No Init!
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }

        //Init the Filter
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['survey-series-de'] &&
          $scope.currentSearchParams.filter['survey-series-en']) {

          //Check with of both filter are active, dependings on acutual language
          if (LanguageService.getCurrentInstantly() === 'de') {
            currentFilterByLanguage =
              $scope.currentSearchParams.filter['survey-series-de'];
          } else {
            currentFilterByLanguage =
              $scope.currentSearchParams.filter['survey-series-en'];
          }

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
                    if (surveySerie[LanguageService.getCurrentInstantly()] ===
                      currentFilterByLanguage) {
                      $scope.currentSurveySeries = surveySerie;
                      return;
                    }
                  });
                }

                //Survey Series was not found, set the last one
                if (!$scope.currentSurveySeries) {
                  $scope.currentSurveySeries = {};
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
          $scope.currentSearchParams.filter['survey-series-de'] =
          surveySeries.de;
          $scope.currentSearchParams.filter['survey-series-en'] =
          surveySeries.en;
        } else {
          //No Survey Series is chosen, delete the Parameter in the URL
          delete $scope.currentSearchParams.filter['survey-series-de'];
          delete $scope.currentSearchParams.filter['survey-series-en'];
        }
        $scope.surveySeriesChangedCallback();
      };

      //Initialize and watch the both Survey Series Filter
      $scope.$watch(['currentSearchParams.filter["survey-series-de"]',
        'currentSearchParams.filter["survey-series-en"]'],
        function() {
          init();
        });
    }
  ]);
