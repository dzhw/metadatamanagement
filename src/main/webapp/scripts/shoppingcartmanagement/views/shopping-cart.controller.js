'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
function($scope, localStorageService, ShoppingCartService) {
  $scope.items = ShoppingCartService.getShoppingCart();
  $scope.archive = function() {
    var oldItems = $scope.items;
    $scope.items = [];
    angular.forEach(oldItems, function(item) {
      if (!item.done) {
        $scope.items.push(item);
      }
    });
    ShoppingCartService.addToShoppingCart($scope.items);
  };});
