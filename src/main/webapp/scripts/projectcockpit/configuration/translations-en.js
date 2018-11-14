'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'projectcockpit': {
        'title':'Project-Cockpit',
        'search': {
          'placeholder': 'Username',
          'header': 'User for project'
        }
    	}
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
