'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('configuration', {
      parent: 'admin',
      url: '/configuration',
      data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'configuration.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/administration/configuration/' +
            'configuration.html.tmpl',
          controller: 'ConfigurationController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('configuration');
          }
        ]
      }
    });
  });
