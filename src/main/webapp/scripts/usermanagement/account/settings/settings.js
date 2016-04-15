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
          translatePartialLoader: ['$translate', '$translatePartialLoader',
              function($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('settings');
                return $translate.refresh();
              }]
        }
      });
    });
