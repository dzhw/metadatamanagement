'use strict';

angular.module('metadatamanagementApp').config(function($stateProvider) {
  $stateProvider.state('shoppingCart', {
    parent: 'site',
    reloadOnSearch: false,
    data: {
      authorities: []
    },
    views: {
      'content@': {
        templateUrl: 'scripts/ordermanagement/views/' +
          'shopping-cart.html.tmpl',
        controller: 'ShoppingCartController',
        controllerAs: 'ctrl'
      }
    },
    onEnter: function($document, $timeout) {
      $timeout(function() {
        var top = $document.find('#top')[0];
        top.scrollIntoView();
      });
    },
    url: '/shopping-cart',
    resolve: {
      order: function(ShoppingCartService, OrderResource) {
        var orderId = ShoppingCartService.getOrderId();
        if (orderId) {
          return OrderResource.get({id: orderId});
        } else {
          return null;
        }
      }
    }
  });
  $stateProvider.state('restoreShoppingCart', {
    parent: 'site',
    reloadOnSearch: false,
    data: {
      authorities: []
    },
    views: {
      'content@': {
        templateUrl: 'scripts/ordermanagement/views/' +
          'shopping-cart.html.tmpl',
        controller: 'ShoppingCartController',
        controllerAs: 'ctrl'
      }
    },
    onEnter: function($document, $timeout) {
      $timeout(function() {
        var top = $document.find('#top')[0];
        top.scrollIntoView();
      });
    },
    url: '/shopping-cart/:id',
    resolve: {
      order: function($state, $stateParams, OrderResource) {
        var order = OrderResource.get({id: $stateParams.id});
        order.$promise.then(null, function(error) {
          if (error.status === 404) {
            $state.go('shoppingCart');
          }
        });
        return order;
      }
    }
  });
});
