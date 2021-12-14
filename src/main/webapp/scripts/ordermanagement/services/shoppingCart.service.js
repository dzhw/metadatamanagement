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

    var _areProductsInShoppingCart = function(products, productList) {
      var toastMessages = [];
      _.forEach(products, function(product, index) {
        var resultIndex = _.findIndex(productList, function(item) {
          if (item.hasOwnProperty('dataPackage') && product
            .hasOwnProperty('dataPackage')) {
            return item.dataPackage.id === products[index].dataPackage.id &&
              item.version === products[index].version &&
              item.accessWay === products[index].accessWay;
          }
          if (item.hasOwnProperty('analysisPackage') && product
            .hasOwnProperty('analysisPackage')) {
            return item.analysisPackage.id === products[index]
                .analysisPackage.id &&
              item.version === products[index].version;
          }
        });
        if (resultIndex !== -1) {
          if (productList[resultIndex].hasOwnProperty('dataPackage')) {
            toastMessages.push({
              messageId: 'shopping-cart.toasts.data-package-already-in-cart',
              messageParams: {id: productList[resultIndex].dataPackage.id}
            });
          }
          if (productList[resultIndex].hasOwnProperty('analysisPackage')) {
            toastMessages.push({
              messageId: 'shopping-cart.toasts.' +
                'analysis-package-already-in-cart',
              messageParams: {id: productList[resultIndex].analysisPackage.id}
            });
          }
          productList.splice(resultIndex, 1);
        }
      });
      if (toastMessages.length) {
        SimpleMessageToastService.openSimpleMessageToasts(toastMessages);
      }
      return productList;
    };

    var _addProductToLocalShoppingCart = function(productList) {
      if (productList.length) {
        products = products.concat(productList);
        localStorageService.set(SHOPPING_CART_KEY, products);
        var toastMessages = [];
        _.forEach(productList, function(item, index) {
          if (item.hasOwnProperty('dataPackage')) {
            toastMessages.push({
              messageId: 'shopping-cart.toasts.data-package-added',
              messageParams: {id: productList[index].dataPackage.id}
            });
          }
          if (item.hasOwnProperty('analysisPackage')) {
            toastMessages.push({
              messageId: 'shopping-cart.toasts.analysis-package-added',
              messageParams: {id: productList[index].analysisPackage.id}
            });
          }
        });
        SimpleMessageToastService.openSimpleMessageToasts(toastMessages);
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

    var _addProductToExistingOrder = function(productList) {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        if (order.state === 'ORDERED') {
          completeOrder();
          _addProductToLocalShoppingCart(productList);
        } else {
          if (productList.length) {
            order.products = order.products.concat(productList);
            order.client = 'MDM';
            OrderResource.update(order).$promise
              .then(function(response) {
                  _setOrderVersion(response.version);
                  _addProductToLocalShoppingCart(productList);
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
      var productList = [];
      if (!Array.isArray(product)) {
        productList.push(product);
      } else {
        productList = productList.concat(product);
      }
      _.forEach(productList, function(item, index) {
        productList[index] = _stripVersionSuffix(item);
      });
      productList = _areProductsInShoppingCart(products, productList);
      if (orderId) {
        synchronizePromise.then(function() {
          _addProductToExistingOrder(productList);
        }, showSynchronizeErrorMessage);
      } else {
        _addProductToLocalShoppingCart(productList);
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
