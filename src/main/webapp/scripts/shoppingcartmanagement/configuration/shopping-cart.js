'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('basket', {
      parent: 'site',
      url: '/basket',
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/shoppingcartmanagement/views/' +
            'shopping-cart.html.tmpl',
          controller: 'ShoppingCartController'
        }
      }
    });
  });
