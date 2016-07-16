'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
    function($q, $rootScope, localStorageService, Principal) {
      var shoppingCartName;
      var getShoppingCart = function() {
        var deferred = $q.defer();
        Principal.identity().then(function(account) {
          if (localStorageService.get(account.login) === null) {
            localStorageService.set(account.login, JSON.stringify([]));
          }
          shoppingCartName = account.login;
          var basket = JSON.parse(localStorageService.get(shoppingCartName));
          deferred.resolve(basket);
        });
        return deferred.promise;
      };

      var addToShoppingCart = function(items) {
        localStorageService.set(shoppingCartName, JSON.stringify(items));
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
