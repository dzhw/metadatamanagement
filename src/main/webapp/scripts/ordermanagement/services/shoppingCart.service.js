/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
  function(OrderResource, localStorageService, $state,
           SimpleMessageToastService, ProjectReleaseService, $rootScope, $q) {

    var SYNCHRONIZE_FAILURE_KEY = 'shopping-cart.error.synchronize';
    var SHOPPING_CART_KEY = 'shoppingCart';
    var ORDER_ID_KEY = 'shoppingCart.orderId';
    var VERSION_KEY = 'shoppingCart.version';

    var products = localStorageService.get(SHOPPING_CART_KEY) || [];
    var orderId = localStorageService.get(ORDER_ID_KEY);
    var version = localStorageService.get(VERSION_KEY);
    var synchronizePromise;

    var showSynchronizeErrorMessage = function() {
      SimpleMessageToastService.openAlertMessageToast(SYNCHRONIZE_FAILURE_KEY);
    };

    var _setOrderVersion = function(newVersion) {
      version = newVersion;
      localStorageService.set(VERSION_KEY, version);
    };

    var _broadcastShoppingCartChanged = function() {
      $rootScope.$broadcast('shopping-cart-changed', products.length);
    };

    var _displayUpdateOrderError = function() {
      SimpleMessageToastService.openAlertMessageToast(
        'shopping-cart.toasts.error-on-saving-order');
    };

    var _displayProductAlreadyInShoppingCart = function(product) {
      if (product.hasOwnProperty('dataPackage') &&
        !product.hasOwnProperty('analysisDataPackage')) {
        SimpleMessageToastService.openSimpleMessageToast(
          'shopping-cart.toasts.data-package-already-in-cart',
          {id: product.dataPackage.id}
        );
      }
      if (product.hasOwnProperty('analysisPackage')) {
        SimpleMessageToastService.openSimpleMessageToast(
          'shopping-cart.toasts.analysis-package-already-in-cart',
          {id: product.analysisPackage.id}
        );
      }
    };

    var _isProductInShoppingCart = function(products, product) {
      return _.findIndex(products, function(item) {
        if (item.hasOwnProperty('dataPackage') && product
          .hasOwnProperty('dataPackage')) {
          return item.dataPackage.id === product.dataPackage.id &&
            item.version === product.version &&
            item.accessWay === product.accessWay;
        }
        if (item.hasOwnProperty('analysisPackage') && product
          .hasOwnProperty('analysisPackage')) {
          return item.analysisPackage.id === product.analysisPackage.id &&
            item.version === product.version;
        }
      }) !== -1;
    };

    var _addProductToLocalShoppingCart = function(product) {
      if (_isProductInShoppingCart(products, product)) {
        _displayProductAlreadyInShoppingCart(product);
      } else {
        products.push(product);
        localStorageService.set(SHOPPING_CART_KEY, products);
        if (product.hasOwnProperty('dataPackage') &&
          !product.hasOwnProperty('analysisDataPackage')) {
          SimpleMessageToastService.openSimpleMessageToast(
            'shopping-cart.toasts.data-package-added',
            {id: product.dataPackage.id});
        }
        if (product.hasOwnProperty('analysisPackage')) {
          SimpleMessageToastService.openSimpleMessageToast(
            'shopping-cart.toasts.analysis-package-added',
            {id: product.analysisPackage.id});
        }
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

    var _stripVersionSuffix = function(product) {
      var normalizedProduct = _.cloneDeep(product);
      normalizedProduct.dataAcquisitionProjectId = ProjectReleaseService
        .stripVersionSuffix(normalizedProduct.dataAcquisitionProjectId);

      if (product.hasOwnProperty('dataPackage')) {
        normalizedProduct.study.id = ProjectReleaseService
          .stripVersionSuffix(normalizedProduct.study.id);
        normalizedProduct.dataPackage.id = ProjectReleaseService
          .stripVersionSuffix(normalizedProduct.dataPackage.id);
      } else {
        normalizedProduct.analysisPackage.id = ProjectReleaseService
          .stripVersionSuffix(normalizedProduct.analysisPackage.id);
      }
      return normalizedProduct;
    };

    var clearOrderData = function() {
      orderId = '';
      localStorageService.set(ORDER_ID_KEY, orderId);
      version = '';
      localStorageService.set(VERSION_KEY, version);
    };

    var completeOrder = function() {
      clearOrderData();
      _clearLocalShoppingCart();
    };

    var _addProductToExistingOrder = function(product) {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        if (order.state === 'ORDERED') {
          completeOrder();
          var normalizedProduct = _stripVersionSuffix(product);
          _addProductToLocalShoppingCart(normalizedProduct);
        } else {
          if (_isProductInShoppingCart(products, product)) {
            _displayProductAlreadyInShoppingCart(product);
          } else {
            order.products.push(product);
            order.client = 'MDM';
            OrderResource.update(order).$promise
              .then(function(response) {
                  _setOrderVersion(response.version);
                  _addProductToLocalShoppingCart(product);
                },
                _displayUpdateOrderError);
          }
        }
      }, _displayUpdateOrderError);
    };

    var _removeProductFromExistingOrder = function(product) {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        if (order.state === 'ORDERED') {
          completeOrder();
          SimpleMessageToastService.openAlertMessageToast(
            'shopping-cart.error.already-completed');
          $state.go('shoppingCart');
        } else {
          var removed = _.remove(order.products, function(productInOrder) {
            if (product.hasOwnProperty('dataPackage') &&
              productInOrder.hasOwnProperty('dataPackage')) {
              return productInOrder.study.id === product.study.id &&
                productInOrder.version === product.version &&
                productInOrder.accessWay === product.accessWay;
            }
            if (product.hasOwnProperty('analysisPackage') &&
              productInOrder.hasOwnProperty('analysisPackage')) {
              return productInOrder.analysisPackage.id === product
                  .analysisPackage.id &&
                productInOrder.version === product.version;
            }
          });

          if (removed.length > 0) {
            order.client = 'MDM';
            OrderResource.update(order).$promise
              .then(function(response) {
                  _setOrderVersion(response.version);
                  _removeProductFromLocalShoppingCart(product);
                },
                _displayUpdateOrderError);
          }
        }
      }, _displayUpdateOrderError);
    };

    var _clearProductsFromExistingOrder = function() {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        order.products = [];
        order.client = 'MDM';
        return OrderResource.update(order).$promise
          .then(function(response) {
            _setOrderVersion(response.version);
            _clearLocalShoppingCart();
          }, _displayUpdateOrderError);
      }, _displayUpdateOrderError);
    };

    var add = function(product) {
      var normalizedProduct = _stripVersionSuffix(product);
      if (orderId) {
        synchronizePromise.then(function() {
          _addProductToExistingOrder(normalizedProduct);
        }, showSynchronizeErrorMessage);
      } else {
        _addProductToLocalShoppingCart(normalizedProduct);
      }
    };

    var remove = function(product) {
      var normalizedProduct = _stripVersionSuffix(product);
      if (orderId) {
        synchronizePromise.then(function() {
          _removeProductFromExistingOrder(normalizedProduct);
        }, showSynchronizeErrorMessage);
      } else {
        _removeProductFromLocalShoppingCart(normalizedProduct);
      }
    };

    var clearProducts = function() {
      if (orderId) {
        synchronizePromise.then(function() {
          _clearProductsFromExistingOrder();
        }, showSynchronizeErrorMessage);
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

    var initShoppingCartProducts = function(initProducts, initOrderId,
                                            initVersion) {
      var copy = _.cloneDeep(initProducts);
      localStorageService.set(SHOPPING_CART_KEY, copy);
      localStorageService.set(ORDER_ID_KEY, initOrderId);
      localStorageService.set(VERSION_KEY, initVersion);
      orderId = initOrderId;
      products = copy;
      version = initVersion;
      _broadcastShoppingCartChanged();
    };

    var getOrderId = function() {
      return orderId;
    };

    var getVersion = function() {
      return version;
    };

    var synchronizeExistingOrder = function() {
      if (orderId) {
        $rootScope.$broadcast('start-ignoring-404');
        var deferred = $q.defer();
        synchronizePromise = deferred.promise;
        OrderResource.get({id: orderId}).$promise.then(function(order) {
          if (order.state === 'ORDERED') {
            completeOrder();
          }
          deferred.resolve();
        }, function(error) {
          if (error.status === 404) {
            completeOrder();
            deferred.resolve();
          } else {
            deferred.reject();
          }
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      } else {
        synchronizePromise = $q.resolve();
      }
    };

    synchronizeExistingOrder();

    return {
      add: add,
      remove: remove,
      getProducts: getProducts,
      count: count,
      clearLocalOrderId: clearOrderData,
      clearProducts: clearProducts,
      completeOrder: completeOrder,
      initShoppingCartProducts: initShoppingCartProducts,
      getOrderId: getOrderId,
      getVersion: getVersion
    };
  });
