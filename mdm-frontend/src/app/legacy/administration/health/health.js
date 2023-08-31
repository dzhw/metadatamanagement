'use strict';

angular.module('metadatamanagementApp').config([
  '$stateProvider',

  function($stateProvider) {
    $stateProvider.state('health', {
      parent: 'admin',
      url: '/health',
      data: {
        authorities: ['ROLE_ADMIN']
      },
      views: {
        'content@': {
          templateUrl: 'scripts/administration/' +
            'health/health.html.tmpl',
          controller: 'HealthController'
        }
      }
    });
  }]);
