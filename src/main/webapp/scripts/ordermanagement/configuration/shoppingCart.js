'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('shoppingCart', {
      parent: 'site',
      url: '/shopping-cart',
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
          var top = angular.element($document.find('#top'));
          var scrollContainer = angular.element(
            $document.find('md-content[du-scroll-container]'));
          scrollContainer.scrollToElementAnimated(top);
        });
      }
    });
  });
