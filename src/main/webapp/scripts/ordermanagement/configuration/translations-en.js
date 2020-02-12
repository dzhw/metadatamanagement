'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'shopping-cart': {
        'title': 'Shopping Cart',
        'detail': {
          'table-title': 'Your selected Data Packages',
          'hint': 'Hint',
          'label': {
            'title': 'Study Title',
            'access-way': 'Access Way',
            'version': 'Version',
            'annotations': 'Annotations',
            'product-options': 'Options of the related Data Package',
            'access-way-of-data-sets': 'Access Way for the Data Sets',
            'version-of-data-sets': 'Version of the Data Sets',
            'available-versions': 'Available Versions',
            'available-access-ways': 'Available Access Ways',
            'number-data-sets': 'Data Sets',
            'number-variables': 'Variables',
            'current': 'current',
            'not-current': 'not current',
            'this-data-product': 'This data package',
            'study-series': 'from study series "{{series}}"',
            'contains': 'contains',
            'variables': '{variables, plural, =0{an unknown amount of variables} =1{one variable} other{{formattedVariables} variables}}',
            'in': 'in',
            'data-sets': '{dataSets, plural, =0{an unknown amount of data sets.} =1{one data set.} other{{formattedDataSets} data sets.}}',
            'study': 'Study',
            'customer-name': 'Your Name',
            'customer-email': 'Your E-mail Address',
            'data-formats': 'The data sets contain data in the following formats:'
          },
          'hints': {
            'name': 'Please let us know your full name.',
            'email': 'Please let us know your e-mail address so that we can contact you.',
            'accessWay': 'Do you need a CUF or SUF and how would you like to work with the data?',
            'version': 'Which version of the data sets do you need?'
          },
          'thank-you': 'Thank you for your interest in our data packages!',
          'dlp-redirect': 'You will be redirected to our service portal in {{seconds}} seconds...',
          'empty-cart-text': 'Your shopping cart is currently empty. You can search for data packages <a href="#!/en/search?type=studies"><strong>here</strong></a> and add those to your shopping cart.',
          'warn-not-current-versions': 'Since you have not decided on the current version of this data package, this system cannot display exact information about the number of variables and data sets in the package.',
          'explain-data-product': 'A data package contains all data sets of a given study which have been prepared for the given access way (download, on-site, remote,...). You can request several data packages of one study with different access ways.',
          'no-final-release': 'The data packages have not yet been created. As soon as they are ready, you can put them in the shopping cart at this point.',
          'variable-not-accessible': 'Although this variable was collected, it is not available in any data package for data protection reasons.',
          'data-not-available': 'This data package is currently not available.',
          'study-tooltip': 'Click to display the study.',
          'study-series-tooltip': 'Click to display all studies of the study series.',
          'data-sets-tooltip': 'Click to display all datasets of this data package.',
          'variables-tooltip': 'Click to display all variables of this data package.'
        },
        'error': {
          'synchronize': 'Unable to synchronize shopping cart with the server.',
          'already-completed': 'The order has already been completed. Your shopping cart has been emptied.',
          'customer': {
            'name': {
              'empty': 'Your name must not be empty.',
              'string-size': 'Your name must not exceed 128 characters.'
            },
            'email': {
              'empty': 'Your e-mail address must not be empty.',
              'string-size': 'Your e-mail address must not exceed 128 characters.',
              'invalid': 'Your e-mail address is invalid.'
            }
          }
        },
        'toasts': {
          'study-added': 'The data package was put into the shopping cart.',
          'study-already-in-cart': 'The data package is already in the shopping cart.',
          'order-has-validation-errors-toast': 'Your form contains invalid data.',
          'error-on-saving-order': 'An error occurred while sending your order.'
        },
        'buttons': {
          'checkout': 'Order',
          'checkout-tooltip': 'Click to order the data packages.',
          'add-study': 'Put in Shopping Cart',
          'add-study-tooltip': 'Click to put the data package with the selected options into the shopping cart.',
          'open-cart': 'Go to Shopping Cart',
          'remove-all': 'Empty Shopping Cart',
          'remove-all-tooltip': 'Click to remove all packages from the shopping cart.',
          'delete-product-tooltip': 'Click to remove the package from the shopping cart.',
          'open-cart-tooltip': 'Click to show the contents of the shopping cart.',
          'close-tooltip': 'Click to close the package selection.',
          'data-package-version-tooltip': 'Click to get more information about versions of data packages',
          'data-package-access-way-tooltip': 'Click to get more information about access ways'
        },
        'version-info': {
          'title': 'Select a Version',
          'content': '<p style="margin-bottom: 0px;">Our data packages are available in a three-digit version. The digits of the version number indicate how big the changes to the data are. You will be notified if changes are made to the first two digits.</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px;"><li>First digit (Major): Changes to the data set (except for changes to variable labels)</li><li>Second digit (Minor): Labels change, metadata changes such as adding more questions or changes to metadata/documentation that affect the analysis.</li><li>Third digit (Patch): Additional dataset formats are provided, adding/deleting language versions.</li></ul><strong>For most data users, the most recent version of the data is relevant.</strong>',
          'close-tooltip': 'Click to close this dialog'
        },
        'access-way-info': {
          'title': 'Select an Access Way',
          'content': '<p style="margin-bottom: 0px;">There are four access paths for our data packages, which on the one hand determine the degree of anonymisation and on the other hand determine the way in which the data can be processed and whether they are approved for use in teaching. Scientific Use Files (SUF) are to be used exclusively for research purposes.</p><p style="margin-bottom: 0px; padding-bottom: 0px;">Our data package configurations are divided into:</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px; padding-top: 0px;"><li><strong>download-cuf</strong> (approval for teaching purposes; strong anonymization; for download)</li><li><strong>download-suf</strong> (strong anonymization; for download)</li><li><strong>remote-desktop-suf</strong> (medium degree of anonymization; access via virtual desktops via Internet)</li><li><strong>onsite-suf</strong> (low degree of anonymization; access only on site in fdz.DZHW in Hanover)</li></ul><p style="margin-bottom: 0px;">Further information can be found <a href="https://fdz.dzhw.eu/en/datennutzung/zugang">here</a>.</p>'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
