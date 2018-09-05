/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
  function(localStorageService, SimpleMessageToastService) {
    var products = localStorageService.get('shoppingCart') || [];

    var add = function(product) {
      var existingIndex = _.findIndex(products, function(item) {
        return _.isEqual(item, product);
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
          {id: product.studyId}
        );
      }
    };

    var remove = function(product) {
      products = _.remove(products, function(item) {
        return _.isEqual(item, product);
      });
      localStorageService.set('shoppingCart', products);
    };

    var replace = function(oldProduct, newProduct) {
      products = _.map(products, function(item) {
        if (_.isEqual(item, oldProduct)) {
          return newProduct;
        }
        return item;
      });
    };

    var clear = function() {
      products = [];
      localStorageService.set('shoppingCart', products);
    };

    var getProducts = function() {
      return _.cloneDeep(products);
    };

    var count = function() {
      return products.length;
    };

    return {
      add: add,
      remove: remove,
      replace: replace,
      getProducts: getProducts,
      count: count,
      clear: clear
    };
  });
