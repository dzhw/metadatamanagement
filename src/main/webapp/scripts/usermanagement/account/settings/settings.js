'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('settings', {
      parent: 'account',
      url: '/settings',
      data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.account.settings'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/settings/' +
            'settings.html.tmpl',
          controller: 'SettingsController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('user.management');
          }
        ]
      }
    });
  });
