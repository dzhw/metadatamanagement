'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'shopping-cart': {
        'title': 'Einkaufswagen',
        'detail': {
          'table-title': 'Ausgewählte Datenprodukte',
          'label': {
            'title': 'Studientitel',
            'access-way': 'Zugangsweg',
            'version': 'Version',
            'study-series': 'Studienreihe',
            'annotations': 'Bemerkungen',
            'product-options': 'Optionen des zugehörigen Datenproduktes',
            'access-way-of-data-sets': 'Zugangsweg zu den Datensätzen',
            'version-of-data-sets': 'Version der Datensätze',
            'available-versions': 'Verfügbare Versionen',
            'available-access-ways': 'Verfügbare Zugangswege',
            'number-data-sets': 'Datensätze',
            'number-variables': 'Variablen'
          },
          'empty-cart-text': 'Ihr Einkaufswagen ist aktuell leer. Sie können <a href="#!/de/search?type=studies"><strong>hier</strong></a> nach Studien suchen und diese Ihrem Einkaufswagen hinzufügen.'
        },
        'toasts': {
          'checkout-coming-soon': 'Der Bestellvorgang wird demnächst implementiert...',
          'study-added': 'Das Datenprodukt der Studie "{{id}}" wurde in den Einkaufswagen gelegt.',
          'study-already-in-cart': 'Das Datenprodukt der Studie "{{id}}" mit den gewählten Optionen ist bereits im Einkaufswagen.'
        },
        'buttons': {
          'checkout': 'Bestellen',
          'add-study': 'In den Einkaufswagen',
          'add-study-tooltip': 'Klicken, um die Studie mit den ausgewählten Optionen in den Einkaufswagen zu legen.',
          'open-cart': 'Zum Einkaufswagen',
          'checkout-tooltip': 'Klicken, um die Datenprodukte zu bestellen.',
          'remove-all': 'Einkaufswagen leeren',
          'remove-all-tooltip': 'Klicken, um alle Datenprodukte aus dem Einkaufswagen zu entfernen.',
          'delete-product-tooltip': 'Klicken, um das Datenprodukt aus dem Einkaufswagen zu entfernen.',
          'open-cart-tooltip': 'Klicken, um die Inhalte des Einkaufswagens anzuzeigen.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
