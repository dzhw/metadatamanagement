'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('register', {
        parent: 'account',
        url: '/register',
        data: {
          authorities: [],
          pageTitle: 'register.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/usermanagement/account/' +
            'register/register.html.tmpl',
            controller: 'RegisterController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
              function($translatePartialLoader) {
                $translatePartialLoader.addPart('register');
              }]
        }
      });
    });
