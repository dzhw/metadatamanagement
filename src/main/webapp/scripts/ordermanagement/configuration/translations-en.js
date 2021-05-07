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
            'title': 'Data Package Title',
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
            'data-package': 'Data Package',
            'customer-name': 'Your Name',
            'customer-email': 'Your E-mail Address',
            'data-formats': 'The data sets contain data in the following formats:',
            'data-languages': 'Furthermore the data sets are available in the following languages:',
            'cite-data-package': 'Cite Data Package:',
            'cite-method-report': 'Cite Method Report:'
          },
          'hints': {
            'name': 'Please let us know your full name.',
            'email': 'Please let us know your e-mail address so that we can contact you.',
            'accessWay': 'Do you need a CUF or SUF and how would you like to work with the data?',
            'version': 'Which version of the data sets do you need?'
          },
          'thank-you': 'Thank you for your interest in our data packages!',
          'dlp-redirect': 'You will be redirected to our service portal in {{seconds}} seconds...',
          'empty-cart-text': 'Your shopping cart is currently empty. You can search for data packages <a href="/en/search?type=data_packages"><strong>here</strong></a> and add those to your shopping cart.',
          'warn-not-current-versions': 'Since you have not decided on the current version of this data package, this system cannot display exact information about the number of variables and data sets in the package.',
          'explain-data-product': 'A data package contains all data sets which have been prepared for the given access way (Download, On-Site or Remote-Desktop). You can request several data packages of one kind with different access ways.',
          'no-final-release': 'The data packages have not yet been created. As soon as they are ready, you can put them in the shopping cart at this point.',
          'variable-not-accessible': 'Although this variable was collected, it is not available in any data package for data protection reasons.',
          'data-package-tooltip': 'Click to display the data package.',
          'study-series-tooltip': 'Click to display all data packages of the study series.',
          'data-sets-tooltip': 'Click to display all datasets of this data package.',
          'variables-tooltip': 'Click to display all variables of this data package.',
          'citation': 'Cite Data Package and Method Report',
          'citation-success-copy-to-clipboard': 'The citation hint was successfully copied to the clipboard.',
          'copy-citation-tooltip': 'Click to copy the citation note to the clipboard.',
          'download-bibtex-tooltip': 'Click to save the citation details in BibTex format.',
          'select-access-way-title': 'Please select an Access Way',
          'select-access-way-for-ordering': 'Please select an access way to add the data package to the shopping cart.',
          'select-access-way-for-citation': 'Please select an access way to cite the data package.',
          'note': 'In case you have problems with the ordering process, please contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>.'
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
          'data-package-added': 'The data package was put into the shopping cart.',
          'data-package-already-in-cart': 'The data package is already in the shopping cart.',
          'order-has-validation-errors-toast': 'Your form contains invalid data.',
          'error-on-saving-order': 'An error occurred while sending your order.'
        },
        'buttons': {
          'checkout': 'Order free of charge',
          'checkout-tooltip': 'Click to order the data packages.',
          'add-data-package': 'Put in Shopping Cart',
          'add-data-package-tooltip': 'Click to put the data package with the selected options into the shopping cart.',
          'choose-data-package-options': 'Click to add a variant of this data package to the shopping cart.',
          'open-cart': 'Go to shopping cart',
          'remove-all': 'Empty Shopping Cart',
          'remove-all-tooltip': 'Click to remove all packages from the shopping cart.',
          'delete-product-tooltip': 'Click to remove the package from the shopping cart.',
          'open-cart-tooltip': 'Click to show the contents of the shopping cart.',
          'open-citation-tooltip': 'Click to get and copy citation information.',
          'open-citation': 'Cite...',
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
          'content': '<p style="margin-bottom: 0px;">There are up to four access ways for our data packages. These differ in terms of:</p><ol style="margin-inline-start: 16px; margin-bottom: 0px; padding-top: 0px;"><li>the purpose of use (<strong>Campus Use File (CUF)</strong> for teaching vs. <strong>Scientific Use File (SUF)</strong> for research),</li><li>the degree of statistical anonymisation (stronger, moderate, lower) and</li><li>the technical way of data access (Download, Remote-Desktop, Onsite) combined with varying degrees of technical and organisational data security and control measures (lower, moderate, stronger).</li></ol><p style="margin-bottom: 0px;">The use of data for download means the least effort in the context of use and should be preferred. If the data package of this access way is <em>not</em> sufficient, the other access ways can also be chosen.</p><p style="margin-bottom: 0px;">Our data package configuration is divided into:</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px; padding-top: 0px;"><li><strong>CUF: Download</strong> for teaching purposes only; higher degree of statistical anonymity; for download after application approval</li><li><strong>SUF: Download</strong>  higher degree of statistical anonymity; for download after conclusion of contract</li><li><strong>SUF: Remote-Desktop</strong>  moderate statistical anonymisation level; access only via virtual desktops via Internet with technical checks after conclusion of contract</li><li><strong>SUF: On-Site</strong> low degree of statistical anonymity; access only on site at the FDZ-DZHW in Hanover with technical checks after conclusion of contract</li></ul><p>More information: <a href="https://www.fdz.dzhw.eu/en/data-usage">https://www.fdz.dzhw.eu/en/data-usage</a></p>'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
