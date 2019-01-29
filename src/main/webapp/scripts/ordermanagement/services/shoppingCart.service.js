/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
  function(OrderResource, StudyResource, localStorageService,
           SimpleMessageToastService, $rootScope) {

    var SHOPPING_CART_KEY = 'shoppingCart';
    var ORDER_ID_KEY = 'shoppingCart.orderId';

    var products = localStorageService.get(SHOPPING_CART_KEY) || [];
    var orderId = localStorageService.get(ORDER_ID_KEY);

    var _broadcastShoppingCartChanged = function() {
      $rootScope.$broadcast('shopping-cart-changed', products.length);
    };

    var _displayUpdateOrderError = function() {
      SimpleMessageToastService.openAlertMessageToast(
        'shopping-cart.toasts.error-on-saving-order');
    };

    var _displayProductAlreadyInShoppingCart = function(product) {
      SimpleMessageToastService.openSimpleMessageToast(
        'shopping-cart.toasts.study-already-in-cart',
        {id: product.study.id}
      );
    };

    var _isProductInShoppingCart = function(products, product) {
      return _.findIndex(products, function(item) {
        return item.study.id === product.study.id &&
          item.version === product.version;
      }) !== -1;
    };

    var _addProductToLocalShoppingCart = function(product) {
      if (_isProductInShoppingCart(products, product)) {
        _displayProductAlreadyInShoppingCart(product);
      } else {
        products.push(product);
        localStorageService.set(SHOPPING_CART_KEY, products);
        SimpleMessageToastService.openSimpleMessageToast(
          'shopping-cart.toasts.study-added',
          {id: product.study.id});
        _broadcastShoppingCartChanged();
      }
    };

    var _removeProductFromLocalShoppingCart = function(product) {
      _.remove(products, function(item) {
        return angular.equals(item, product);
      });
      localStorageService.set(SHOPPING_CART_KEY, products);
      _broadcastShoppingCartChanged();
    };

    var _clearLocalShoppingCart = function() {
      products = [];
      localStorageService.set(SHOPPING_CART_KEY, products);
      _broadcastShoppingCartChanged();
    };

    var _addProductToExistingOrder = function(product) {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        if (_isProductInShoppingCart(products, product)) {
          _displayProductAlreadyInShoppingCart(product);
        } else {
          StudyResource.get({id: product.study.id}).$promise
            .then(function(study) {
              var newProduct = {
                dataAcquisitionProjectId: product.dataAcquisitionProjectId,
                study: study,
                accessWay: product.accessWay,
                version: product.version
              };

              order.products.push(newProduct);
              OrderResource.update(order).$promise
                .then(_addProductToLocalShoppingCart.bind(null, product),
                  _displayUpdateOrderError);
            }, _displayUpdateOrderError);
        }
      }, _displayUpdateOrderError);
    };

    var _removeProductFromExistingOrder = function(product) {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        var removed = _.remove(order.products, function(productInOrder) {
          return productInOrder.study.id === product.study.id &&
            productInOrder.version === product.version;
        });

        if (removed.length > 0) {
          OrderResource.update(order).$promise
            .then(_removeProductFromLocalShoppingCart.bind(null, product),
              _displayUpdateOrderError);
        }
      }, _displayUpdateOrderError);
    };

    var _clearProductsFromExistingOrder = function() {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        order.products = [];
        return OrderResource.update(order).$promise
          .then(_clearLocalShoppingCart, _displayUpdateOrderError);
      }, _displayUpdateOrderError);
    };

    var add = function(product) {
      if (orderId) {
        _addProductToExistingOrder(product);
      } else {
        _addProductToLocalShoppingCart(product);
      }
    };

    var remove = function(product) {
      if (orderId) {
        _removeProductFromExistingOrder(product);
      } else {
        _removeProductFromLocalShoppingCart(product);
      }
    };

    var clearProducts = function() {
      if (orderId) {
        _clearProductsFromExistingOrder();
      } else {
        _clearLocalShoppingCart();
      }
    };

    var getProducts = function() {
      return _.cloneDeep(products);
    };

    var count = function() {
      return products.length;
    };

    var initShoppingCartProducts = function(initProducts, initOrderId) {
      var copy = _.cloneDeep(initProducts);
      localStorageService.set(SHOPPING_CART_KEY, copy);
      localStorageService.set(ORDER_ID_KEY, initOrderId);
      orderId = initOrderId;
      products = copy;
      _broadcastShoppingCartChanged();
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
      localStorageService.set(SHOPPING_CART_KEY, migratedProducts);
      products = migratedProducts;
    };

    var clearOrderId = function() {
      orderId = '';
      localStorageService.set(ORDER_ID_KEY, orderId);
    };

    var completeOrder = function() {
      clearOrderId();
      _clearLocalShoppingCart();
    };

    var getOrderId = function() {
      return orderId;
    };

    return {
      add: add,
      remove: remove,
      getProducts: getProducts,
      count: count,
      clearLocalOrderId: clearOrderId,
      clearProducts: clearProducts,
      completeOrder: completeOrder,
      initShoppingCartProducts: initShoppingCartProducts,
      migrateStoredData: migrateStoredData,
      getOrderId: getOrderId
    };
  });
