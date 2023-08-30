/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionSearchFilterController', [
    '$scope', 'QuestionSearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$location', '$q',
    function($scope, QuestionSearchService, $timeout,
      CurrentProjectService, $rootScope, $location, $q) {
      // prevent question changed events during init
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
          $scope.currentSearchParams.filter.question) {
          $rootScope.$broadcast('start-ignoring-404');
          QuestionSearchService.findOneById(
            $scope.currentSearchParams.filter.question,
            ['id', 'masterId', 'questionText', 'number']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentQuestion = result;
              } else {
                $scope.currentQuestion = {
                  id: $scope.currentSearchParams.filter.question
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentQuestion = {
                  id: $scope.currentSearchParams.filter.question
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
          $scope.currentSearchParams.filter.question = question.id;
        } else {
          delete $scope.currentSearchParams.filter.question;
        }
        $scope.questionChangedCallback();
      };

      $scope.searchQuestions = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'question');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return QuestionSearchService.findQuestionTitles(searchText,
           cleanedFilter, $scope.type, $scope.query, $scope.projectId)
          .then(function(questionTitles) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = questionTitles;
            cache.type = $scope.type;
            cache.query = $scope.query;
            cache.projectId = $scope.projectId;
            return questionTitles;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter.question', function() {
        init();
      });
    }
  ]);
