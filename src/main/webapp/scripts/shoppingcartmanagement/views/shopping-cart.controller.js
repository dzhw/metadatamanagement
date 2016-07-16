'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
function($scope, localStorageService, ShoppingCartService) {

  ShoppingCartService.getShoppingCart().then(function(basket) {
    $scope.todos = basket;
  });
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
