'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('basket', {
      parent: 'site',
      url: '/basket',
      data: {
        authorities: [],
        pageTitle: 'Shopping cart'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/shoppingcartmanagement/views/' +
            'shopping-cart.html.tmpl',
          controller: 'ShoppingCartController'
        }
      },
      resolve: {
        mainTranslatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('disclosure');//should be changed..
          }
        ]
      }
    });
  });
