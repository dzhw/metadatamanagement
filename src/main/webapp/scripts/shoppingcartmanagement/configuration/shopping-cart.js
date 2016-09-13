'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('basket', {
      parent: 'site',
      url: '/basket',
      data: {
        authorities: [],
        pageTitle: 'notepad-management.home.title'
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
            $translatePartialLoader.addPart('notepad.management');
          }
        ]
      }
    });
  });
