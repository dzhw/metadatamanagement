'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function($scope, localStorageService, ShoppingCartService) {

      ShoppingCartService.getShoppingCart().then(function(basket) {
        $scope.todos = basket;
      });

      $scope.addTodo = function(id) {
        $scope.todos.push({
          id: id,
          text: 'title',
          date:  new Date()
        });
        ShoppingCartService.addToShoppingCart($scope.todos);
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
