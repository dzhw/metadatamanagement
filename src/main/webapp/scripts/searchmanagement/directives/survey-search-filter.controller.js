/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySearchFilterController', [
    '$scope', 'SearchDao', 'SurveySearchService', '$timeout',
    'CurrentProjectService',
    function($scope, SearchDao, SurveySearchService, $timeout,
      CurrentProjectService) {
      // prevent survey changed events during init
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
          $scope.currentSearchParams.filter.survey) {
          SurveySearchService.findOneById(
            $scope.currentSearchParams.filter.survey).promise
            .then(function(result) {
              if (result) {
                $scope.currentSurvey = {_source: result};
              } else {
                $scope.currentSurvey = {
                  _source: {
                    id: $scope.currentSearchParams.filter.survey
                  }
                };
              }
            }, function() {
                $scope.currentSurvey = {
                  _source: {
                    id: $scope.currentSearchParams.filter.survey
                  }
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
          $scope.currentSearchParams.filter.survey = survey._source.id;
        } else {
          delete $scope.currentSearchParams.filter.survey;
        }
        $scope.surveyChangedCallback();
      };

      $scope.searchSurveys = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'survey');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
            CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            currentProjectId, cleanedFilter,
            'surveys',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastProjectId = currentProjectId;
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      $scope.$watch('currentSearchParams.filter.survey', function() {
        init();
      });
    }
  ]);
