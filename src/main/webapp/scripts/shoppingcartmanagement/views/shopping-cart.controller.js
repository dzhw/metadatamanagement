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
      SimpleMessageToastService.openSimpleMessageToast(null, null);
    };

    $scope.removeFromShoppingCart = function() {
      $scope.basket.items = ShoppingCartService
      .removeFromShoppingCart($scope.basket.items);
    };
  });
