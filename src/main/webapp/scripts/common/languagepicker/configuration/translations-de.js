'use strict';

angular.module('metadatamanagementApp').config(
  function ($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'language-picker': {
          'error': {
            'required': 'Es muss mindestens eine Sprache ausgewählt sein'
          },
          'label': 'Sprachen',
          'placeholder': 'Sprachen auswählen'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }
);
