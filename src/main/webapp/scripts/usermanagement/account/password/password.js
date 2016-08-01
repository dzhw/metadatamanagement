'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('password', {
      parent: 'account',
      url: '/password',
      data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.account.password'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/' +
            'password/password.html.tmpl',
          controller: 'PasswordController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('password');
          }
        ]
      }
    });
  });
