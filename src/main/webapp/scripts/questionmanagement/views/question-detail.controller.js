
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, entity, localStorageService, ShoppingCartService,
    DialogService) {
      $scope.question = entity;
      console.log($scope.question);
      $scope.todos = ShoppingCartService.getShoppingCart();
      $scope.showVariables = function() {
        DialogService.showDialog($scope.question.variableIds);
      };
      $scope.addTodo = function(id) {
        var lookup = {};
        for (var i = 0, len = $scope.todos.length; i < len; i++) {
          lookup[$scope.todos[i].id] = $scope.todos[i];
        }
        if (ShoppingCartService.searchInShoppingCart(id) === 'notFound') {
          $scope.todos.push({
            id: id,
            text: 'title',
            date:  new Date()
          });
          ShoppingCartService.addToShoppingCart($scope.todos);
        }
      };

      $scope.archive = function() {
        var oldTodos = $scope.todos;
        $scope.todos = [];
        angular.forEach(oldTodos, function(todo) {
          if (!todo.done) {
            $scope.todos.push(todo);
          }
        });
        ShoppingCartService.addToShoppingCart($scope.todos);
      };});
