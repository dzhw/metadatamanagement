'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('logs', {
      parent: 'admin',
      url: '/logs',
      data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'logs.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/administration/logs/logs.html.tmpl',
          controller: 'LogsController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('logs');
          }
        ]
      }
    });
  });
