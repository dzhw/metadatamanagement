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
        }
    	}
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
