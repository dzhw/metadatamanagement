'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('login', {
      parent: 'account',
      url: '/login',
      data: {
        authorities: [],
        pageTitle: 'login.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/login/' +
          'login.html.tmpl',
          controller: 'LoginController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translate', '$translatePartialLoader',
          function($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('login');
            return $translate.refresh();
          }
        ]
      }
    });
  });
