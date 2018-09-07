/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
  function(localStorageService, SimpleMessageToastService, $rootScope) {
    var products = localStorageService.get('shoppingCart') || [];

    var add = function(product) {
      var existingIndex = _.findIndex(products, function(item) {
        return angular.equals(item, product);
      });
      if (existingIndex >= 0) {
        SimpleMessageToastService.openSimpleMessageToast(
          'shopping-cart.toasts.study-already-in-cart',
          {id: product.studyId}
        );
      } else {
        products.push(product);
        localStorageService.set('shoppingCart', products);
        SimpleMessageToastService.openSimpleMessageToast(
          'shopping-cart.toasts.study-added',
          {id: product.studyId});
        $rootScope.$broadcast('shopping-cart-changed', products.length);
      }
    };

    var remove = function(product) {
      _.remove(products, function(item) {
        return angular.equals(item, product);
      });
      localStorageService.set('shoppingCart', products);
      $rootScope.$broadcast('shopping-cart-changed', products.length);
    };

    var replace = function(oldProduct, newProduct) {
      products = _.map(products, function(item) {
        if (angular.equals(item, oldProduct)) {
          return newProduct;
        }
        return item;
      });
      $rootScope.$broadcast('shopping-cart-changed', products.length);
    };

    var clear = function() {
      products = [];
      localStorageService.set('shoppingCart', products);
      $rootScope.$broadcast('shopping-cart-changed', products.length);
    };

    var getProducts = function() {
      return _.cloneDeep(products);
    };

    var count = function() {
      return products.length;
    };

    var checkout = function() {
      SimpleMessageToastService.openAlertMessageToast(
        'shopping-cart.toasts.checkout-coming-soon');
    };

    return {
      add: add,
      remove: remove,
      replace: replace,
      getProducts: getProducts,
      count: count,
      clear: clear,
      checkout: checkout
    };
  });
