'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('settings', {
      parent: 'account',
      url: '/settings',
      data: {
        authorities: ['ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER',
          'ROLE_ADMIN']
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/settings/' +
            'settings.html.tmpl',
          controller: 'SettingsController'
        }
      }
    });
  });
