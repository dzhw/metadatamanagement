'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'common': {
        'language-picker': {
          'error': {
            'required': 'Es muss mindestens eine Sprache ausgewählt sein',
            'md-require-match': 'Diese Sprache konnte nicht gefunden werden.'
          },
          'label': 'Sprachen',
          'placeholder': 'Sprachen auswählen'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);
