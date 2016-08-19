'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
    function($rootScope, localStorageService) {
      var basket = [];
      var getShoppingCart = function() {
        if (localStorageService.get('shoppingCart') === null) {
          localStorageService.set('shoppingCart', JSON.stringify([]));
        }
        basket = JSON.parse(localStorageService.get('shoppingCart'));
        return basket;
      };

      var addToShoppingCart = function(items) {
        localStorageService.set('shoppingCart', JSON.stringify(items));
        $rootScope.$broadcast('itemsCount', items.length);
      };

      var searchInShoppingCart = function(item) {
        var search = {};
        for (var i = 0, len = basket.length; i < len; i++) {
          search[basket[i].id] = basket[i];
        }
        try {
          return search[item].id;
        } catch (e) {
          return 'notFound';
        }
      };

      var removeFromShoppingCart = function(json) {
        return json;
      };
      return {
        getShoppingCart: getShoppingCart,
        addToShoppingCart: addToShoppingCart,
        removeFromShoppingCart: removeFromShoppingCart,
        searchInShoppingCart: searchInShoppingCart
      };
    });
