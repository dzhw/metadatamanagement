'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('health', {
      parent: 'admin',
      url: '/health',
      data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'health.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/administration/' +
            'health/health.html.tmpl',
          controller: 'HealthController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('health');
          }
        ]
      }
    });
  });
