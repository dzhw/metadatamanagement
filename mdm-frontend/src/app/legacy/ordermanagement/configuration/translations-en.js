'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'shopping-cart': {
        'title': 'Shopping Cart',
        'detail': {
          'table-title': 'Your selected Products',
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
            'this-analysis-product': 'This analysis package',
            'study-series': 'from study series "{{series}}"',
            'contains': 'contains',
            'following': 'for the following',
            'software-packages': '{packages, plural, =0{an unknown amount of software packages} =1{software package} other{software packages}}',
            'variables': '{variables, plural, =0{an unknown amount of variables} =1{one variable} other{{formattedVariables} variables}}',
            'scripts': '{scripts, plural, =0{an unknown amount of scripts} =1{one script} other{{formattedScripts} scripts}}',
            'additionally': 'Additionally it contains',
            'custom-data-packages': '{packages, plural, =0{an unknown amount of custom data packages} =1{one custom data package} other{{formattedPackages} custom data packages}}',
            'in': 'in',
            'data-sets': '{dataSets, plural, =0{an unknown amount of data sets.} =1{one data set.} other{{formattedDataSets} data sets.}}',
            'data-package': 'Data Package',
            'customer-name': 'Your Name',
            'customer-email': 'Your E-mail Address',
            'data-formats': 'The data sets contain data in the following formats:',
            'data-languages': 'Furthermore the data sets are available in the following languages:',
            'cite-data-package': 'Cite Data Package:',
            'cite-analysis-package': 'Cite Analysis Package:',
            'cite-method-report': 'Cite Method Report:',
            'cite-questionnaire': 'Cite Questionnaire/Variable Questionnaire:'
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
          'analysis-package-tooltip': 'Click to display the analysis package.',
          'study-series-tooltip': 'Click to display all data packages of the study series.',
          'data-sets-tooltip': 'Click to display all datasets of this data package.',
          'variables-tooltip': 'Click to display all variables of this data package.',
          'scripts-tooltip': 'Click to display all scripts of this analysis package.',
          'custom-data-packages-tooltip': 'Click to display all custom data packages of this analysis package.',
          'software-packages-tooltip': 'Click to display all software packages of this analysis package.',
          'citation': 'Cite Data Package, Method Report and Questionnaire/Variable Questionnaire',
          'citation-success-copy-to-clipboard': 'The citation hint was successfully copied to the clipboard.',
          'copy-citation-tooltip': 'Click to copy the citation note to the clipboard.',
          'download-bibtex-tooltip': 'Click to save the citation details in BibTex format.',
          'citation-node': 'We ask you to check the Data and Methods Report to see whether there is information on other sources for the specific question to be cited. If such information is available, we suggest that you also cite the original source(s) mentioned in the data and methods report in accordance with good scientific practice. When quoting the complete text of a question, for example within an article or when reusing the question for your own research projects or questionnaires, please note that there may be further license agreements. Please check the sources in this regard.',
          'select-access-way-title': 'Please select an Access Way',
          'select-access-way-for-ordering': 'Please select an access way to add the data package to the shopping cart.',
          'select-access-way-for-citation': 'Please select an access way to cite the data package.',
          'note': 'In case you have problems with the ordering process, please contact <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>.',
          'maintenance-hint': 'On Wednesday, 25.01. and Thursday, 26.01. the order process will only be available to a limited extent due to maintenance work.',
          'analysis-package': {
            'citation': 'Cite Analysis Package'
          }
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
          'data-package-added': 'The data package {{id}} was put into the shopping cart.',
          'data-package-already-in-cart': 'The data package {{id}} is already in the shopping cart.',
          'analysis-package-added': 'The analysis package {{id}} was put into the shopping cart.',
          'analysis-package-already-in-cart': 'The analysis package {{id}} is already in the shopping cart.',
          'order-has-validation-errors-toast': 'Your form contains invalid data.',
          'error-on-saving-order': 'An error occurred while sending your order.'
        },
        'buttons': {
          'checkout': 'Order free of charge',
          'checkout-tooltip': 'Click to order the data packages.',
          'add-data-package': 'Put in Shopping Cart',
          'add-data-package-tooltip': 'Click to put the data package with the selected options into the shopping cart.',
          'add-analysis-package-tooltip': 'Click to put the analysis package with the selected options into the shopping cart.',
          'choose-data-package-options': 'Click to add a variant of this data package to the shopping cart.',
          'choose-analysis-package-options': 'Click to add a variant of this analysis package to the shopping cart.',
          'open-cart': 'Go to shopping cart',
          'remove-all': 'Empty Shopping Cart',
          'remove-all-tooltip': 'Click to remove all packages from the shopping cart.',
          'delete-product-tooltip': 'Click to remove the package from the shopping cart.',
          'open-cart-tooltip': 'Click to show the contents of the shopping cart.',
          'open-citation-tooltip': 'Click to get and copy citation information.',
          'open-citation': 'Cite...',
          'open-export': 'Export metadata...',
          'beta-hint': 'All formats generated via da|ra (beta-Status)',
          'open-export-tooltip': 'Click to export the metadata of this data package.',
          'close-tooltip': 'Click to close the package selection.',
          'data-package-version-tooltip': 'Click to get more information about versions of data packages',
          'data-package-tooltip': 'Click to get more information about data packages',
          'analysis-package-tooltip': 'Click to get more information about analysis packages',
          'related-publications-tooltip': 'Click to get more information about publications',
          'analysis-package-version-tooltip': 'Click to get more information about versions of analysis packages',
          'data-package-access-way-tooltip': 'Click to get more information about access ways',
          'export-ddi-variables': 'Export variable metadata',
          'export-ddi-variables-tooltip': 'Click to download the metadata of all variables as a DDI codebook XML.'
        },
        'version-info': {
          'title': 'Select a Version',
          'content': '<p style="margin-bottom: 0px;">Our data packages are available in a three-digit version. The digits of the version number indicate how big the changes to the data are. You will be notified if changes are made to the first two digits.</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px;"><li>First digit (Major): Changes to the data set (except for changes to variable labels)</li><li>Second digit (Minor): Labels change, metadata changes such as adding more questions or changes to metadata/documentation that affect the analysis.</li><li>Third digit (Patch): Additional dataset formats are provided, adding/deleting language versions.</li></ul><strong>For most data users, the most recent version of the data is relevant.</strong>',
          'close-tooltip': 'Click to close this dialog',
          'analysis-package': {
            'content': '<p style="margin-bottom: 0px;">Our analysis packages are available in a three-digit version. The digits of the version number indicate how big the changes to the data are. You will be notified if changes are made to the first two digits.</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px;"><li>First digit (Major): Changes/Adding to/of one or more analysis scripts and/or one or more custom data packages.</li><li>Second digit (Minor): Changes to metadata/documentation.</li><li>Third digit (Patch): Adding/deleting language versions.</li></ul><strong>For most data users, the most recent version of the data is relevant.</strong>'
          }
        },
        'access-way-info': {
          'title': 'Select an Access Way',
          'content': '<p style="margin-bottom: 0px;">There are up to four access ways for our data packages:</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px; padding-top: 0px;"><li><strong>CUF: Download</strong> created for academic teaching and training purposes; very high statistical anonymization level; available for download after application approval</li><li><strong>SUF: Download</strong> created for scientific research purposes; high statistical anonymization level; available for download after conclusion of data use agreement</li><li><strong>SUF: Remote-Desktop</strong> created for scientific research purposes; moderate statistical anonymization level; available via internet using virtual desktop with input/output checks after conclusion of data use agreement</li><li><strong>SUF: On-Site</strong> created for scientific research purposes; low statistical anonymization level; available on site at the FDZ-DZHW in Hanover with input/output checks after conclusion of data use agreement</li></ul><p>More information: <a href="https://www.fdz.dzhw.eu/en/data-usage">https://www.fdz.dzhw.eu/en/data-usage</a></p>'
        }
      },
      'order-menu': {
        'back-to-project': 'Back to last editing state',
        'back-to-project-tooltip': 'Click to return to the last editing state'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }]);
