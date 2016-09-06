
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, $rootScope, localStorageService,
      ShoppingCartService, $stateParams, DialogService,
      QuestionSearchResource, blockUI, entity, $q) {
      $scope.question = entity;
      $scope.predecessors = [];
      $scope.successors = [];

      /* function to load only texts and ids for successors and predecessors */
      var loadQuestionTextOnly = function(items) {
        var deferred = $q.defer();
        var itemsAsString = '"' + items + '"';
        itemsAsString = itemsAsString.replace(/[\(\)\[\]{}'"]/g, '');
        QuestionSearchResource.findByIdIn({ids: itemsAsString})
        .$promise.then(function(customQuestions) {
          deferred.resolve(customQuestions);
        });
        return deferred.promise;
      };

      var checkInvalidQuestionIds = function(allIds, customItems) {
        var tempQuestions = [];
        allIds.forEach(function(id) {
          for (var i = 0; i < customItems.length; i++) {
            if (customItems[i].id === id) {
              tempQuestions.push(customItems[i]);
              break;
            }else {
              if (i === (customItems.length - 1)) {
                var notFoundQuestion = {
                  id: id,
                  name: 'notFoundQuestion',
                  number: '-',
                  questionText: 'not-found'
                };
                tempQuestions.push(notFoundQuestion);
              }
            }
          }
        });
        return tempQuestions;
      };
      $scope.$watch('question', function() {
        if ($scope.question.$resolved) {
          loadQuestionTextOnly($scope.question.successor)
          .then(function(customSuccesors) {
            $scope.successors = checkInvalidQuestionIds(
              $scope.question.successor, customSuccesors._embedded.questions);
          });
          loadQuestionTextOnly($scope.question.predecessor)
          .then(function(customPredecessors) {
            $scope.predecessors = checkInvalidQuestionIds(
              $scope.question.predecessor,
              customPredecessors._embedded.questions);
          });
        }
      }, true);

      /* function to open dialog for variables */
      $scope.showVariables = function() {
        blockUI.start();
        DialogService.showDialog($scope.question.variableIds, 'variable',
          ['label', 'name'], $rootScope.currentLanguage);
      };

      /* get all items from localStorage */
      $scope.markedItems = ShoppingCartService.getShoppingCart();

      /* add new  item to localStorage */
      $scope.addToNotepad = function(id) {
        var lookup = {};
        for (var i = 0, len = $scope.markedItems.length; i < len; i++) {
          lookup[$scope.markedItems[i].id] = $scope.markedItems[i];
        }
        if (ShoppingCartService.searchInShoppingCart(id) === 'notFound') {
          $scope.markedItems.push({
            id: id,
            text: 'title',
            date:  new Date()
          });
          ShoppingCartService.addToShoppingCart($scope.markedItems);
        }
      };
    });
