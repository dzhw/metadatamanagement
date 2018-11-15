'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'projectcockpit': {
        'title': 'Projekt-Cockpit',
        'search': {
          'placeholder': 'Benutzername',
          'header': 'Benutzer für Projekt'
        },
        'alert': {
          'title': 'Achtung',
          'noproject': 'Kein Projekt ausgewählt.',
          'close': 'Okay'
        },
        'label': {
          'ROLE_USER': 'User',
          'ROLE_ADMIN': 'Admin',
          'ROLE_DATA_PROVIDER': 'Data Provider',
          'ROLE_PUBLISHER': 'Publisher'
        },
        'button': {
          'save': 'Klicken, um die Anpassungen zu speichern.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
