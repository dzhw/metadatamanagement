'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'shopping-cart': {
        'title': 'Shopping Cart',
        'detail': {
          'table-title': 'Your selected Data Package',
          'personal-details': 'Personal Details',
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
            'customer-email': 'Your E-mail Address'
          },
          'hints': {
            'name': 'Please let us know your full name.',
            'email': 'Please let us know your e-mail address so that we can contact you.',
            'accessWay': 'Do you need a CUF or SUF and how would you like to work with the data?',
            'version': 'Which version of the data sets do you need?'
          },
          'thank-you': 'Thank you for your interest in our data packages!',
          'order-placed-text': 'We have sent you a confirmation email and will contact you shortly.',
          'empty-cart-text': 'Your shopping cart is currently empty. You can search for data packages <a href="#!/en/search?type=studies"><strong>here</strong></a> and add those to your shopping cart.',
          'warn-not-current-versions': 'Since you have not decided on the current version of this data package, this system cannot display exact information about the number of variables and data sets in the package.',
          'explain-data-product': 'A data package always contains all data sets of a study that have been prepared for the selected access way (download, on-site, remote,...). You can request several data sets of the same study with different access ways.',
          'no-final-release': 'This data package have not yet been finalized. As soon as it is ready, you will be able to put it in the shopping cart at this point.',
          'data-not-available': 'This data package is currently not available.',
          'study-tooltip': 'Click to display the study.',
          'study-series-tooltip': 'Click to display all studies of the study series.',
          'data-sets-tooltip': 'Click to display all datasets of this data package.',
          'variables-tooltip': 'Click to display all variables of this data package.'
        },
        'error': {
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
          'customer-has-validation-errors-toast': 'You have not yet provided all the necessary information about yourself.',
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
          'content': '<p style="margin-bottom: 0px;">Unsere Datenpakete liegen dreistellig versioniert vor. Die Stellen der Versionsnummer legen nahe, wie groß die Änderungen an den Daten sind. Bei Änderungen an den ersten beiden Stellen werden Sie benachrichtigt.</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px;"><li>Erste Stelle (Major): Änderungen am Datensatz (abgesehen von Änderungen der Variablenlabels)</li><li>Zweite Stelle (Minor): Label ändern sich, Metadatenänderungen wie Hinzufügen weiterer Fragen oder Änderungen der Metadaten/Dokumentation, die Auswirkungen auf die Analyse haben.</li><li>Dritte Stelle (Patch): Zusätzliche Datensatzformate werden bereitgestellt, Hinzufügen/Löschen von Sprachversionen.</li></ul><strong>Für die meisten Datennutzer/-innen ist die aktuellste Version der Daten relevant.</strong>',
          'close-tooltip': 'Click to close this dialog'
        },
        'access-way-info': {
          'title': 'Select an Access Way',
          'content': '<p style="margin-bottom: 0px;">Für unsere Datenpakete gibt es vier Zugangswege, die zum einen den Grad der Anonymisierung bestimmen und zum anderen bestimmen auf welchem Weg die Daten verarbeitet werden können und ob sie zur Verwendung in der Lehre zugelassen sind. Scientific Use Files (SUF) sind ausschließlich zum Zweck der Forschung zu verwenden.</p><p style="margin-bottom: 0px; padding-bottom: 0px;">Unsere Datenpaketkonfigurationen sind gegliedert in:</p><ul style="list-style-type: disc; margin-inline-start: 16px; margin-bottom: 0px; padding-top: 0px;"><li>download-cuf (Zulassung für Lehrzwecke; starke Anonymisierung; zum Download)</li><li>download-suf (stark anonymisiert; zum Download)</li><li>remote-desktop-suf (mittlerer Anonymisierungsgrad; Zugang über virtuelle Desktops per Internet)</li><li>onsite-suf (geringer Anonymisierungsgrad; Zugang nur vor Ort im fdz.DZHW in Hannover)</li></ul><p style="margin-bottom: 0px;">Weitere Informationen finden Sie <a href="https://fdz.dzhw.eu/datennutzung/zugang">hier</a>.</p>'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
