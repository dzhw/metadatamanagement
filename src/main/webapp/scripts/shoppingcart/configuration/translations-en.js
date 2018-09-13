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
            'about': 'about',
            'this-data-product': 'This data product',
            'study-series': 'from study series {{series}}',
            'contains': 'contains',
            'variables': '{variables, plural, =0{an unknown amount of variables} =1{one variable} other{{formattedVariables} variables}}',
            'in': 'in',
            'data-sets': '{dataSets, plural, =0{an unknown amount of data sets} =1{one data set} other{{formattedDataSets} data sets}}'
          },
          'empty-cart-text': 'Your shopping cart is currently empty. You can search for data products <a href="#!/en/search?type=studies"><strong>here</strong></a> and add those to your shopping cart.',
          'warn-not-current-versions': 'If you have not decided on the current version of a data product, this system cannot display exact information about the number of variables and data sets in the product.',
          'explain-data-product': 'A data product contains all data sets of a given study which have been prepared for the given access way (download, on-site, remote,...). You can request several data products as well as the same study several times with different access ways.',
          'no-final-release': 'The data products have not yet been created. As soon as they are ready, you can put them in the shopping cart at this point.',
          'variable-not-accessible': 'Although this variable was collected, it is not available in any data product for data protection reasons.',
          'data-not-available': 'This data product is currently not available.',
          'study-tooltip': 'Click to display the study.',
          'study-series-tooltip': 'Click to display all studies of the study series.',
          'data-sets-tooltip': 'Click to display all datasets of this data product.',
          'variables-tooltip': 'Click to display all variables of this data product.'
        },
        'toasts': {
          'checkout-coming-soon': 'Checkout is coming soon...',
          'study-added': 'The data product was put into the shopping cart.',
          'study-already-in-cart': 'The data product is already in the shopping cart.'
        },
        'buttons': {
          'checkout': 'Order',
          'checkout-tooltip': 'Click to order the data products.',
          'add-study': 'Put in Shopping Cart',
          'add-study-tooltip': 'Click to put the data product with the selected options into the shopping cart.',
          'open-cart': 'Go to Shopping Cart',
          'remove-all': 'Empty Shopping Cart',
          'remove-all-tooltip': 'Click to remove all products from the shopping cart.',
          'delete-product-tooltip': 'Click to remove the product from the shopping cart.',
          'open-cart-tooltip': 'Click to show the contents of the shopping cart.',
          'close-tooltip': 'Click to close the product selection.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
