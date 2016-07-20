'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
function($scope, localStorageService, ShoppingCartService,
  SimpleMessageToastService) {
  $scope.basket = {
    firstname: '',
    lastname: '',
    email: '',
    items: []
  };

  $scope.basket.items = ShoppingCartService.getShoppingCart();

  $scope.isDisabled = function() {
    return $scope.basket.items.length === 0;
  };

  $scope.submitForm = function() {
    console.log($scope.basket);
    SimpleMessageToastService.openSimpleMessageToast(null, null);
  };

  $scope.removeFromShoppingCart = function() {
    var oldItems = $scope.basket.items;
    $scope.basket.items = [];
    angular.forEach(oldItems, function(item) {
      if (!item.done) {
        $scope.basket.items.push(item);
      }
    });
    ShoppingCartService.addToShoppingCart($scope.basket.items);
  };
});
