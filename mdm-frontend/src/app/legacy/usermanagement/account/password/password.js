'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('password', {
      parent: 'account',
      url: '/password',
      data: {
        authorities: ['ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER',
          'ROLE_ADMIN']
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/' +
            'password/password.html.tmpl',
          controller: 'PasswordController'
        }
      }
    });
  });
