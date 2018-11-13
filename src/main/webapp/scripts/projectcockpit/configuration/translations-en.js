'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'projectcockpit': {
        'title':'Project-Cockpit',
    	}
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
