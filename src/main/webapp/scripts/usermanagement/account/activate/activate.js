'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('activate', {
      parent: 'account',
      url: '/activate?key',
      data: {
        authorities: [],
        pageTitle: 'user-management.activate.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/activate/' +
            'activate.html.tmpl',
          controller: 'ActivationController'
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
