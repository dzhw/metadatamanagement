/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
  function(localStorageService, SimpleMessageToastService, $rootScope) {
    var products = localStorageService.get('shoppingCart') || [];
    var orderId = localStorageService.get('shoppingCart.orderId');
    var shoppingCartVersion = localStorageService.get('shoppingCart.version');

    var add = function(product) {
      var existingIndex = _.findIndex(products, function(item) {
        return angular.equals(item, product);
      });
      if (existingIndex >= 0) {
        SimpleMessageToastService.openSimpleMessageToast(
          'shopping-cart.toasts.study-already-in-cart',
          {id: product.study.id}
        );
      } else {
        products.push(product);
        localStorageService.set('shoppingCart', products);
        SimpleMessageToastService.openSimpleMessageToast(
          'shopping-cart.toasts.study-added',
          {id: product.study.id});
        $rootScope.$broadcast('shopping-cart-changed', products.length);
      }
    };

    var remove = function(product) {
      _.remove(products, function(item) {
        return angular.equals(item, product);
      });
      localStorageService.set('shoppingCart', products);
      if (products.length === 0) {
        orderId = '';
        localStorageService.set('shoppingCart.orderId', orderId);
        shoppingCartVersion = '';
        localStorageService
          .set('shoppingCart.version', shoppingCartVersion);

      }
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
      orderId = '';
      shoppingCartVersion = '';
      localStorageService.set('shoppingCart', products);
      localStorageService.set('shoppingCart.orderId', orderId);
      localStorageService.set('shoppingCart.version', shoppingCartVersion);
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

    var initShoppingCartProducts = function(initProducts, initOrderId,
                                            orderVersion) {

      var copy = _.cloneDeep(initProducts);
      localStorageService.set('shoppingCart', copy);
      localStorageService.set('shoppingCart.orderId', initOrderId);
      localStorageService.set('shoppingCart.version', orderVersion);
      shoppingCartVersion = orderVersion;
      orderId = initOrderId;
      products = copy;
    };

    var migrateStoredData = function() {
      var storedProducts = this.getProducts();
      var migratedProducts = [];
      storedProducts.forEach(function(product) {
        if (_.has(product, 'projectId')) {
          var newProduct = {
            dataAcquisitionProjectId: product.projectId,
            accessWay: product.accessWay,
            version: product.version,
            study: {
              id: product.studyId
            }
          };
          migratedProducts.push(newProduct);
        } else {
          migratedProducts.push(product);
        }
      });
      localStorageService.set('shoppingCart', migratedProducts);
      products = migratedProducts;
    };

    var getOrderId = function() {
      return orderId;
    };

    var getShoppingCartVersion = function() {
      return shoppingCartVersion;
    };

    return {
      add: add,
      remove: remove,
      replace: replace,
      getProducts: getProducts,
      count: count,
      clear: clear,
      checkout: checkout,
      initShoppingCartProducts: initShoppingCartProducts,
      migrateStoredData: migrateStoredData,
      getOrderId: getOrderId,
      getShoppingCartVersion: getShoppingCartVersion
    };
  });
