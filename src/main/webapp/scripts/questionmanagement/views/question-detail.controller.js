
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, $rootScope, localStorageService,
      ShoppingCartService, $stateParams, DialogService, VariableResource,
      QuestionResource,
      $resource) {
      $scope.question = {};
      $scope.variables = [];
      $scope.predecessors = [];
      $scope.successors = [];
      var counter = {};

      var loadVariables = function() {
        if (counter.variables < $scope.question.variableIds.length) {
          VariableResource
          .get({id: $scope.question.variableIds[counter.variables]})
          .$promise.then(function(resource) {
            $scope.variables.push(resource);
            counter.variables++;
            loadVariables();
          });
        }
      };
      var loadSuccesors = function() {
        if (counter.successors < $scope.question.successor.length) {
          $resource('api/questions/:id')
          .get({id: $scope.question.successor[counter.successors],
            projection: 'complete'})
          .$promise.then(function(resource) {
            $scope.successors.push(resource);
            counter.successors++;
            loadSuccesors();
          });
        }
      };
      var loadPredecessors = function() {
        if (counter.predecessors < $scope.question.predecessor.length) {
          $resource('api/questions/:id')
          .get({id: $scope.question.predecessor[counter.predecessors],
            projection: 'complete'})
          .$promise.then(function(resource) {
            $scope.predecessors.push(resource);
            counter.predecessors++;
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
        counter = {
          variables: 0,
          successors: 0,
          predecessors: 0
        };
        $scope.question = question;
        loadVariables();
        loadSuccesors();
        loadPredecessors();
      });
      /*Shopping Cart*/
      $scope.markedItems = ShoppingCartService.getShoppingCart();
      $scope.showVariables = function() {
        DialogService.showDialog($scope.variables,
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

      $scope.archive = function() {
        var oldTodos = $scope.markedItems;
        $scope.markedItems = [];
        angular.forEach(oldTodos, function(todo) {
          if (!todo.done) {
            $scope.markedItems.push(todo);
          }
        });
        ShoppingCartService.addToShoppingCart($scope.markedItems);
      };
    });
