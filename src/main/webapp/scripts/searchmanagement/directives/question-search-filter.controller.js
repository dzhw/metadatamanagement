/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionSearchFilterController', [
    '$scope', 'SearchDao', 'QuestionSearchService',
    function($scope, SearchDao, QuestionSearchService) {
      // prevent question changed events during init
      var initializing = true;
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
        return SearchDao.search(searchText, 1,
            undefined, _.omit($scope.currentSearchParams.filter, 'question'),
            'questions',
            100).then(function(data) {
              return data.hits.hits;
            }
          );
      };
      init();
    }
  ]);
