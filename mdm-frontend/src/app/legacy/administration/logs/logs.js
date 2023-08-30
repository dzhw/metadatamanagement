'use strict';

angular.module('metadatamanagementApp').config([
  '$stateProvider',

  function($stateProvider) {
    $stateProvider.state('logs', {
      parent: 'admin',
      url: '/logs',
      data: {
        authorities: ['ROLE_ADMIN']
      },
      views: {
        'content@': {
          templateUrl: 'scripts/administration/logs/logs.html.tmpl',
          controller: 'LogsController'
        }
      }
    });
  }]);
