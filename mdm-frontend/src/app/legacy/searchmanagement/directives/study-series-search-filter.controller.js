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
        language: null,
        filter: null,
        type: null,
        query: null,
        projectId: null,
        searchResult: null
      };
      var currentFilterByLanguage;
      var filterKeys = ['study-series-de', 'study-series-en',
        'parent-study-de', 'parent-study-en'];

      //Search Method for Study Series, call Elasticsearch
      $scope.searchStudySeries = function(searchText, language) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          filterKeys);

        if (searchText === cache.searchText &&
            language === cache.language &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return DataPackageSearchService.findStudySeriesFilterOptions(searchText,
          cleanedFilter, language, $scope.type, $scope.query, $scope.projectId)
          .then(function(studySeries) {
            cache.searchText = searchText;
            cache.language = language;
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
        var filter = $scope.currentSearchParams.filter || {};
        var property = filter['parent-study-' + currentLanguage] ?
          'parent-study-' : 'study-series-';
        if (filter[property + currentLanguage]) {

          //Check with of both filter are active, dependings on acutual language
          currentFilterByLanguage =
            filter[property + currentLanguage];

          //Search Study Series and for Validation
          $scope.searchStudySeries(currentFilterByLanguage, currentLanguage)
            .then(function(studySeries) {

                $scope.currentStudySeries = _.find(studySeries,
                  function(series) {
                    return series[currentLanguage] === currentFilterByLanguage &&
                      series.isParentStudy === (property === 'parent-study-');
                  });

                //Study Series was not found check the language
                if (!$scope.currentStudySeries) {
                  $scope.currentStudySeries =
                    filter[property + currentLanguage];
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

          property = filter['parent-study-' + i18nAnotherEnding] ?
            'parent-study-' : 'study-series-';
          if (filter[property + i18nAnotherEnding]) {
            $scope.searchStudySeries(
              filter[property + i18nAnotherEnding], i18nAnotherEnding)
              .then(function(studySeries) {
                var translatedSeries = _.find(studySeries, function(series) {
                  return series[i18nAnotherEnding] ===
                    filter[property + i18nAnotherEnding] &&
                    series.isParentStudy === (property === 'parent-study-');
                });
                if (translatedSeries) {
                  $scope.currentStudySeries = translatedSeries;

                  $scope.currentSearchParams.filter[property +
                    i18nActualEnding] = translatedSeries[i18nActualEnding];
                  $scope.selectedFilters.push(property +
                    i18nActualEnding);
                  delete $scope.selectedFilters[_.indexOf(
                    $scope.selectedFilters,
                    property + i18nAnotherEnding)
                    ];
                  delete $scope.currentSearchParams.filter[property +
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
        $scope.currentSearchParams.filter = _.omit(
          $scope.currentSearchParams.filter, filterKeys);

        //Set the Study Series Filter in the URL
        if (studySeries) {
          var property = studySeries.isParentStudy ?
            'parent-study-' : 'study-series-';
          $scope.currentSearchParams.filter[property +
            $scope.currentLanguage] = studySeries[$scope.currentLanguage];
        }
        $scope.studySeriesChangedCallback();
      };

      //Initialize and watch the current Study Series Filter
      $scope.$watchGroup(['currentLanguage'].concat(filterKeys.map(
        function(key) { return 'currentSearchParams.filter["' + key + '"]'; }
      )),
        function() {
          init($scope.currentLanguage);
        });
    }
  ]);
