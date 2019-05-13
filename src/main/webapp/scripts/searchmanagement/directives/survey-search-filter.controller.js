/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySearchFilterController', [
    '$scope', 'SurveySearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$location', '$q',
    function($scope, SurveySearchService, $timeout,
      CurrentProjectService, $rootScope, $location, $q) {
      // prevent survey changed events during init
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
      var init = function() {
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter.survey) {
          $rootScope.$broadcast('start-ignoring-404');
          SurveySearchService.findOneById(
            $scope.currentSearchParams.filter.survey,
            ['id', 'masterId', 'title']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentSurvey = result;
              } else {
                $scope.currentSurvey = {
                  id: $scope.currentSearchParams.filter.survey
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentSurvey = {
                  id: $scope.currentSearchParams.filter.survey
                };
                $timeout(function() {
                  $scope.surveyFilterForm.surveyFilter.$setValidity(
                    'md-require-match', false);
                }, 500);
                $scope.surveyFilterForm.surveyFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(survey) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (survey) {
          $scope.currentSearchParams.filter.survey = survey.id;
        } else {
          delete $scope.currentSearchParams.filter.survey;
        }
        $scope.surveyChangedCallback();
      };

      $scope.searchSurveys = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'survey');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return SurveySearchService.findSurveyTitles(searchText, cleanedFilter,
           $scope.type, $scope.query, $scope.projectId)
          .then(function(surveyTitles) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = surveyTitles;
            cache.type = $scope.type;
            cache.query = $scope.query;
            cache.projectId = $scope.projectId;
            return surveyTitles;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter.survey', function() {
        init();
      });
    }
  ]);
