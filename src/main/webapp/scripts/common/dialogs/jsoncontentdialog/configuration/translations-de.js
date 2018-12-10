'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'dialogs': {
          'jsoncontentdialog': {
            'close': 'Schließen',
            'tooltip': 'Daten als JSON anzeigen'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
