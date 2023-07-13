/* global _ */
'use strict';
angular.module('metadatamanagementApp')
  .controller('SurveyMethodSearchFilterController', [
  '$scope',
  '$location',
  '$q',
  '$timeout',
  'CurrentProjectService',
  'SurveySearchService',
    function($scope, $location, $q, $timeout, CurrentProjectService,
             SurveySearchService) {
      // prevent survey method changed events during init
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

      //Search after survey methods, call Elasticsearch
      $scope.searchSurveyMethods = function(searchText, language) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'survey-method-' + language);

        if (searchText === cache.searchText &&
          $scope.type === cache.type &&
          _.isEqual(cache.filter, cleanedFilter) &&
          $scope.query === cache.query &&
          $scope.projectId === cache.projectId
        ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return SurveySearchService.findSurveyMethodFilterOptions(searchText,
          cleanedFilter, $scope.type, $scope.query, $scope.projectId)
          .then(function(surveyMethods) {
              cache.searchText = searchText;
              cache.filter = _.cloneDeep(cleanedFilter);
              cache.searchResult = surveyMethods;
              cache.type = $scope.type;
              cache.query = $scope.query;
              cache.projectId = $scope.projectId;
              return surveyMethods;
            }
          );
      };

      //Init the de and en filter of survey methods
      var init = function(currentLanguage) {
        //Just a change? No Init!
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }

        //Init the Filter
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['survey-method-' +
          currentLanguage]) {

          //Check with of both filter are active, depending on actual language
          currentFilterByLanguage =
            $scope.currentSearchParams.filter['survey-method-' +
            currentLanguage];

          //Search and validate survey method
          $scope.searchSurveyMethods(currentFilterByLanguage, currentLanguage)
            .then(function(surveyMethods) {

              //Just one survey method exists
              if (surveyMethods.length === 1) {
                $scope.currentSurveyMethod = surveyMethods[0];
                return;
              } else if (surveyMethods.length > 1) {
                //Standard case, there are many survey methods
                surveyMethods.forEach(function(surveyMethod) {
                  if (surveyMethod[currentLanguage] ===
                    currentFilterByLanguage) {
                    $scope.currentSurveyMethod = surveyMethod;
                    return;
                  }
                });
              }

              //Survey method was not found check the language
              if (!$scope.currentSurveyMethod) {
                $scope.currentSurveyMethod =
                  $scope.currentSearchParams.filter['survey-method-' +
                  currentLanguage];
                $timeout(function() {
                  $scope.surveyMethodFilterForm.surveyMethodFilter
                    .$setValidity('md-require-match', false);
                }, 500);
                $scope.surveyMethodFilterForm.surveyMethodFilter
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
            $scope.currentSearchParams.filter['survey-method-' +
            i18nAnotherEnding]) {
            $scope.searchSurveyMethods(
              $scope.currentSearchParams.filter['survey-method-' +
              i18nAnotherEnding], i18nAnotherEnding)
              .then(function(surveyMethods) {
                if (surveyMethods) {
                  $scope.currentSurveyMethod = surveyMethods[0];

                  $scope.currentSearchParams.filter['survey-method-' +
                  i18nActualEnding] = surveyMethods[0][i18nActualEnding];
                  $scope.selectedFilters.push('survey-method-' +
                    i18nActualEnding);
                  delete $scope.selectedFilters[_.indexOf(
                    $scope.selectedFilters,
                    'survey-method-' + i18nAnotherEnding)
                    ];
                  delete $scope.currentSearchParams.filter['survey-method-' +
                  i18nAnotherEnding];
                  return;
                }
              });
          }
          initializing = false;
        }
      };

      //Set the filter, if the user changed the survey method Filter
      $scope.onSelectionChanged = function(surveyMethod) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }

        //Set the survey method filter in the URL
        if (surveyMethod) {
          $scope.currentSearchParams.filter['survey-method-' +
          $scope.currentLanguage] = surveyMethod[$scope.currentLanguage];
        } else {
          //No survey method is chosen, delete the Parameter in the URL
          delete $scope.currentSearchParams.filter['survey-method-' +
          $scope.currentLanguage];
        }
        $scope.surveyMethodChangedCallback();
      };

      //Initialize and watch the current survey method Filter
      $scope.$watch('currentSearchParams.filter["survey-method-' +
        $scope.currentLanguage + '"]',
        function() {
          init($scope.currentLanguage);
        });
    }]);

