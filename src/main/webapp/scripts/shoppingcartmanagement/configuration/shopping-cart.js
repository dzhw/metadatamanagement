'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('basket', {
      parent: 'site',
      url: '/basket',
      data: {
        authorities: [],
        //TODO SHould be an i18n String
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
            //TODO should be changed...
            $translatePartialLoader.addPart('disclosure');
          }
        ]
      }
    });
  });
