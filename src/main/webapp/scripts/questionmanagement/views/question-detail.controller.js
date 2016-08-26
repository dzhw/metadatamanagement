
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, $rootScope, localStorageService,
      ShoppingCartService, $stateParams, DialogService,
      QuestionResource, blockUI, $resource) {

      $scope.question = {};
      $scope.predecessors = [];
      $scope.successors = [];
      $scope.counter = {
        successors: 0,
        predecessors: 0,
        sum: 0
      };
      $scope.startBlockUI = function() {
        blockUI.start();
      };
      var loadSuccesors = function() {
        if ($scope.counter.successors < $scope.question.successor.length) {
          $resource('api/questions/:id')
          .get({id: $scope.question.successor[$scope.counter.successors],
            projection: 'complete'})
          .$promise.then(function(resource) {
            $scope.successors.push(resource);
            $scope.counter.successors++;
            loadSuccesors();
          }, function() {
            var notFoundQuestion = {
              id: $scope.question.successor[$scope.counter.successors],
              name: 'notFoundQuestion'
            };
            $scope.successors.push(notFoundQuestion);
            $scope.counter.successors++;
            loadSuccesors();
          });
        }
      };
      var loadPredecessors = function() {
        if ($scope.counter.predecessors < $scope.question.predecessor.length) {
          $resource('api/questions/:id')
          .get({id: $scope.question.predecessor[$scope.counter.predecessors],
            projection: 'complete'})
          .$promise.then(function(resource) {
            $scope.predecessors.push(resource);
            $scope.counter.predecessors++;
            loadPredecessors();
          }, function() {
            var notFoundQuestion = {
              id: $scope.question.predecessor[$scope.counter.predecessors],
              name: 'notFoundQuestion',
              number: '-',
              questionText: 'not-found'
            };
            $scope.predecessors.push(notFoundQuestion);
            $scope.counter.predecessors++;
            loadPredecessors();
          });
        }
      };

      /* load question resource. the entity resolver dont work properly by
      backbutton click because the state and controller are not .einitialized.
      The controller should be always reinitialized
      see https://github.com/angular-ui/ui-router/issues/582
      */
      QuestionResource.get({id: $stateParams.id})
      .$promise.then(function(question) {
        $scope.counter.sum = question.successor.length +
        question.predecessor.length;
        $scope.question = question;
        loadSuccesors();
        loadPredecessors();
      });
      $scope.$watch('counter', function() {
        var tempSum = $scope.counter.successors +
                      $scope.counter.predecessors;
        if (($scope.counter.sum === tempSum) && (tempSum > 0)) {
          blockUI.stop();
        }
      }, true);

      /*Shopping Cart*/
      $scope.markedItems = ShoppingCartService.getShoppingCart();
      $scope.showVariables = function() {
        blockUI.start();
        DialogService.showDialog($scope.question.variableIds,
          $rootScope.currentLanguage);
      };
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
