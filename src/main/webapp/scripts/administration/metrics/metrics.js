'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('metrics', {
      parent: 'admin',
      url: '/metrics',
      data: {
        authorities: ['ROLE_ADMIN']
      },
      views: {
        'content@': {
          templateUrl: 'scripts/administration/metrics/' +
            'metrics.html.tmpl',
          controller: 'MetricsController'
        }
      }
    });
  });
