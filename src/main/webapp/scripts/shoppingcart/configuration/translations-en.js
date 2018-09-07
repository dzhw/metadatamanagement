'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'shopping-cart': {
        'title': 'Shopping Cart',
        'detail': {
          'table-title': 'Your selected Data Products',
          'label': {
            'title': 'Study Title',
            'access-way': 'Access Way',
            'version': 'Version',
            'study-series': 'Study Series',
            'annotations': 'Annotations',
            'product-options': 'Options of the related Data Product',
            'access-way-of-data-sets': 'Access Way for the Data Sets',
            'version-of-data-sets': 'Version of the Data Sets',
            'available-versions': 'Available Versions',
            'available-access-ways': 'Available Access Ways',
            'number-data-sets': 'Data Sets',
            'number-variables': 'Variables',
            'current': 'current',
            'not-current': 'not current',
            'unknown': 'unknown'
          },
          'empty-cart-text': 'Your shopping cart is currently empty. You can search for data products <a href="#!/en/search?type=studies"><strong>here</strong></a> and add those to your shopping cart.',
          'warn-not-current-versions': 'If you have not decided on the current version of a data product, this system cannot display exact information about the number of variables and data sets in the product.',
          'explain-data-product': 'A data product contains all data sets of a given study which have been prepared for the given access way (download, on-site, remote,...). You can request several data products as well as the same study several times with different access ways.'
        },
        'toasts': {
          'checkout-coming-soon': 'Checkout is coming soon...',
          'study-added': 'The data product of study "{{id}}" was put in the shopping cart.',
          'study-already-in-cart': 'The data product of study "{{id}}" with the selected options is already in the shopping cart.'
        },
        'buttons': {
          'checkout': 'Order',
          'checkout-tooltip': 'Click to order the data products.',
          'add-study': 'Put in Shopping Cart',
          'add-study-tooltip': 'Click to put the study with the selected options in the shopping cart.',
          'open-cart': 'Go to Shopping Cart',
          'remove-all': 'Empty Shopping Cart',
          'remove-all-tooltip': 'Click to remove all products from the shopping cart.',
          'delete-product-tooltip': 'Click to remove the product from the shopping cart.',
          'open-cart-tooltip': 'Click to show the contents of the shopping cart.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
