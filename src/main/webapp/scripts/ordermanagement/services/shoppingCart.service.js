/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ShoppingCartService',
  function(OrderResource, StudyResource, localStorageService,
           SimpleMessageToastService, ProjectReleaseService, $rootScope) {

    var SHOPPING_CART_KEY = 'shoppingCart';
    var ORDER_ID_KEY = 'shoppingCart.orderId';
    var VERSION_KEY = 'shoppingCart.version';

    var products = localStorageService.get(SHOPPING_CART_KEY) || [];
    var orderId = localStorageService.get(ORDER_ID_KEY);
    var version = localStorageService.get(VERSION_KEY);

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
      SimpleMessageToastService.openSimpleMessageToast(
        'shopping-cart.toasts.study-already-in-cart',
        {id: product.study.id}
      );
    };

    var _isProductInShoppingCart = function(products, product) {
      return _.findIndex(products, function(item) {
        return item.study.id === product.study.id &&
          item.version === product.version &&
          item.accessWay === product.accessWay;
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
                .then(function(response) {
                    _setOrderVersion(response.version);
                    _addProductToLocalShoppingCart(product);
                  },
                  _displayUpdateOrderError);
            }, _displayUpdateOrderError);
        }
      }, _displayUpdateOrderError);
    };

    var _removeProductFromExistingOrder = function(product) {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        var removed = _.remove(order.products, function(productInOrder) {
          return productInOrder.study.id === product.study.id &&
            productInOrder.version === product.version &&
          productInOrder.accessWay === product.accessWay;
        });

        if (removed.length > 0) {
          OrderResource.update(order).$promise
            .then(function(response) {
                _setOrderVersion(response.version);
                _removeProductFromLocalShoppingCart(product);
              },
              _displayUpdateOrderError);
        }
      }, _displayUpdateOrderError);
    };

    var _clearProductsFromExistingOrder = function() {
      OrderResource.get({id: orderId}).$promise.then(function(order) {
        order.products = [];
        return OrderResource.update(order).$promise
          .then(function(response) {
            _setOrderVersion(response.version);
            _clearLocalShoppingCart();
          }, _displayUpdateOrderError);
      }, _displayUpdateOrderError);
    };

    var _stripVersionSuffix = function(product) {
      var normalizedProduct = _.cloneDeep(product);

      normalizedProduct.dataAcquisitionProjectId = ProjectReleaseService
        .stripVersionSuffix(normalizedProduct.dataAcquisitionProjectId);

      normalizedProduct.study.id = ProjectReleaseService
        .stripVersionSuffix(normalizedProduct.study.id);
      return normalizedProduct;
    };

    var add = function(product) {
      var normalizedProduct = _stripVersionSuffix(product);
      if (orderId) {
        _addProductToExistingOrder(normalizedProduct);
      } else {
        _addProductToLocalShoppingCart(normalizedProduct);
      }
    };

    var remove = function(product) {
      var normalizedProduct = _stripVersionSuffix(product);
      if (orderId) {
        _removeProductFromExistingOrder(normalizedProduct);
      } else {
        _removeProductFromLocalShoppingCart(normalizedProduct);
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

    var getOrderId = function() {
      return orderId;
    };

    var getVersion = function() {
      return version;
    };

    return {
      add: add,
      remove: remove,
      getProducts: getProducts,
      count: count,
      clearLocalOrderId: clearOrderData,
      clearProducts: clearProducts,
      completeOrder: completeOrder,
      initShoppingCartProducts: initShoppingCartProducts,
      migrateStoredData: migrateStoredData,
      getOrderId: getOrderId,
      getVersion: getVersion
    };
  });
