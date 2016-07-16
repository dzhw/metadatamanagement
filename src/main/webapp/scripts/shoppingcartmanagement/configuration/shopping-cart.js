'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('basket', {
      parent: 'site',
      url: '/basket',
      data: {
        authorities: ['ROLE_USER'],
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
        mainTranslatePartialLoader: ['$translate',
          '$translatePartialLoader',
          function($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('disclosure');//should be changed..
            return $translate.refresh();
          }
        ]
      }
    });
  });
