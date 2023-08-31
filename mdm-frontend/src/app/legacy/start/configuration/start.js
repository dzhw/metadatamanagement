'use strict';

angular.module('metadatamanagementApp').config(["$stateProvider",
  function($stateProvider) {
    $stateProvider.state('start', {
      parent: 'site',
      url: '/start',
      reloadOnSearch: false,
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/start/views/start.html.tmpl',
          controller: 'StartController'
        }
      }
    });
  }]);
