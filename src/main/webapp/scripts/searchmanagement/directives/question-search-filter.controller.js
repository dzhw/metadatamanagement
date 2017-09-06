/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionSearchFilterController', [
    '$scope', 'SearchDao', 'QuestionSearchService', '$timeout',
    'CurrentProjectService',
    function($scope, SearchDao, QuestionSearchService, $timeout,
      CurrentProjectService) {
      // prevent question changed events during init
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
          $scope.currentSearchParams.filter.question) {
          QuestionSearchService.findOneById(
            $scope.currentSearchParams.filter.question).promise
            .then(function(result) {
              if (result) {
                $scope.currentQuestion = {_source: result};
              } else {
                $scope.currentQuestion = {
                  _source: {
                    id: $scope.currentSearchParams.filter.question
                  }
                };
              }
            }, function() {
                $scope.currentQuestion = {
                  _source: {
                    id: $scope.currentSearchParams.filter.question
                  }
                };
                $timeout(function() {
                  $scope.questionFilterForm.questionFilter.$setValidity(
                    'md-require-match', false);
                }, 500);
                $scope.questionFilterForm.questionFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(question) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (question) {
          $scope.currentSearchParams.filter.question = question._source.id;
        } else {
          delete $scope.currentSearchParams.filter.question;
        }
        $scope.questionChangedCallback();
      };

      $scope.searchQuestions = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'question');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
            CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            currentProjectId, cleanedFilter,
            'questions',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastProjectId = currentProjectId;
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      $scope.$watch('currentSearchParams.filter.question', function() {
        init();
      });
    }
  ]);
