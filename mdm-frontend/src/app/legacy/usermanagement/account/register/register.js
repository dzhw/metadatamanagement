'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('register', {
      parent: 'account',
      url: '/register',
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/' +
            'register/register.html.tmpl',
          controller: 'RegisterController'
        }
      }
    });
  });
