/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionSearchFilterController', [
    '$scope', 'SearchDao', 'QuestionSearchService', '$timeout',
    function($scope, SearchDao, QuestionSearchService, $timeout) {
      // prevent question changed events during init
      var initializing = true;
      var lastSearchText;
      var lastFilter;
      var lastSearchResult;
      var init = function() {
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
                });
                $scope.questionFilterForm.questionFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(question) {
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (question) {
          $scope.currentSearchParams.filter.question = question._source.id;
        } else {
          delete $scope.currentSearchParams.filter.question;
        }
        if (!initializing) {
          $scope.questionChangedCallback();
        }
        initializing = false;
      };

      $scope.searchQuestions = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'question');
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter)) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            undefined, cleanedFilter,
            'questions',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };

      init();
    }
  ]);
