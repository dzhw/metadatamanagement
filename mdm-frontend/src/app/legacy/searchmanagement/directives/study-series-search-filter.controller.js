/* global _ */
/* @Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySeriesSearchFilterController', [
    '$scope', 'DataPackageSearchService', '$timeout', '$location',
    'CurrentProjectService', '$q',
    function($scope, DataPackageSearchService, $timeout, $location,
      CurrentProjectService, $q) {
      // prevent study-series changed events during init
      var initializing = true;
      var selectionChanging = false;
      var cache = {
        searchText: null,
        filter: null,
        type: null,
        query: null,
        projectId: null,
        searchResult: null
      };
      var currentFilterByLanguage;

      //Search Method for Study Series, call Elasticsearch
      $scope.searchStudySeries = function(searchText, language) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'study-series-' + language);

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return DataPackageSearchService.findStudySeries(searchText,
          cleanedFilter, language, $scope.type, $scope.query, $scope.projectId)
          .then(function(studySeries) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = studySeries;
            cache.type = $scope.type;
            cache.query = $scope.query;
            cache.projectId = $scope.projectId;
            return studySeries;
          }
        );
      };

      //Init the de and en filter of study series
      var init = function(currentLanguage) {
        //Just a change? No Init!
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }

        //Init the Filter
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['study-series-' +
            currentLanguage]) {

          //Check with of both filter are active, dependings on acutual language
          currentFilterByLanguage =
            $scope.currentSearchParams.filter['study-series-' +
              currentLanguage];

          //Search Study Series and for Validation
          $scope.searchStudySeries(currentFilterByLanguage, currentLanguage)
            .then(function(studySeries) {

                //Just one Study Series exists
                if (studySeries.length === 1) {
                  $scope.currentStudySeries = studySeries[0];
                  return;
                } else if (studySeries.length > 1) {
                  //Standard Case, there are many Study Series
                  studySeries.forEach(function(surveySerie) {
                    if (surveySerie[currentLanguage] ===
                        currentFilterByLanguage) {
                      $scope.currentStudySeries = surveySerie;
                      return;
                    }
                  });
                }

                //Study Series was not found check the language
                if (!$scope.currentStudySeries) {
                  $scope.currentStudySeries =
                    $scope.currentSearchParams.filter['study-series-' +
                    currentLanguage];
                  $timeout(function() {
                    $scope.studySeriesFilterForm.studySeriesFilter
                      .$setValidity('md-require-match', false);
                  }, 500);
                  $scope.studySeriesFilterForm.studySeriesFilter
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

          if ($scope.currentSearchParams.filter &&
              $scope.currentSearchParams.filter['study-series-' +
                i18nAnotherEnding]) {
            $scope.searchStudySeries(
              $scope.currentSearchParams.filter['study-series-' +
              i18nAnotherEnding], i18nAnotherEnding)
              .then(function(studySeries) {
                if (studySeries) {
                  $scope.currentStudySeries = studySeries[0];

                  $scope.currentSearchParams.filter['study-series-' +
                    i18nActualEnding] = studySeries[0][i18nActualEnding];
                  $scope.selectedFilters.push('study-series-' +
                    i18nActualEnding);
                  delete $scope.selectedFilters[_.indexOf(
                    $scope.selectedFilters,
                    'study-series-' + i18nAnotherEnding)
                    ];
                  delete $scope.currentSearchParams.filter['study-series-' +
                    i18nAnotherEnding];
                  return;
                }
              });
          }
          initializing = false;
        }
      };

      //Set the Filter, if the user changed the Study Series Filter
      $scope.onSelectionChanged = function(studySeries) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }

        //Set the Study Series Filter in the URL
        if (studySeries) {
          $scope.currentSearchParams.filter['study-series-' +
            $scope.currentLanguage] = studySeries[$scope.currentLanguage];
        } else {
          //No Study Series is chosen, delete the Parameter in the URL
          delete $scope.currentSearchParams.filter['study-series-' +
            $scope.currentLanguage];
        }
        $scope.studySeriesChangedCallback();
      };

      //Initialize and watch the current Study Series Filter
      $scope.$watch('currentSearchParams.filter["study-series-' +
        $scope.currentLanguage + '"]',
        function() {
          init($scope.currentLanguage);
        });
    }
  ]);
