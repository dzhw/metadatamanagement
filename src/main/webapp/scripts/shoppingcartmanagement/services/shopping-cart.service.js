'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
    function($rootScope, localStorageService) {
      var getShoppingCart = function() {
        if (localStorageService.get('shoppingCart') === null) {
          localStorageService.set('shoppingCart', JSON.stringify([]));
        }
        var basket = JSON.parse(localStorageService.get('shoppingCart'));
        return basket;
      };

      var addToShoppingCart = function(items) {
        localStorageService.set('shoppingCart', JSON.stringify(items));
        $rootScope.$broadcast('itemsCount', items.length);
      };

      var removeFromShoppingCart = function(json) {
        return json;
      };
      return {
        getShoppingCart: getShoppingCart,
        addToShoppingCart: addToShoppingCart,
        removeFromShoppingCart: removeFromShoppingCart
      };
    });
