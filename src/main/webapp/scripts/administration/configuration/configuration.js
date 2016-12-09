'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('configuration', {
      parent: 'admin',
      url: '/configuration',
      data: {
        authorities: ['ROLE_ADMIN']
      },
      views: {
        'content@': {
          templateUrl: 'scripts/administration/configuration/' +
            'configuration.html.tmpl',
          controller: 'ConfigurationController'
        }
      }
    });
  });
