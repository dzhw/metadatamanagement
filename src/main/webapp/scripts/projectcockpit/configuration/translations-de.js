'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'projectcockpit': {
        'title': 'Projekt-Cockpit',
    	}
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });
