'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('login', {
      parent: 'account',
      url: '/login',
      data: {
        authorities: [],
        pageTitle: 'user-management.login.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/login/' +
            'login.html.tmpl',
          controller: 'LoginController'
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
