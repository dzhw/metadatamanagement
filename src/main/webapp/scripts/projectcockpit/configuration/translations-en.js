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
        },
        'alert': {
          'title': 'Attention',
          'noproject': 'No project selected.',
          'close': 'Okay'
        },
        'label': {
          'ROLE_USER': 'User',
          'ROLE_ADMIN': 'Admin',
          'ROLE_DATA_PROVIDER': 'Data Provider',
          'ROLE_PUBLISHER': 'Publisher'
        },
        'button': {
          'save': 'Click to save the changes.'
        },
        'list': {
          'empty': 'No users are assigned to this project.'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
