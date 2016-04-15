'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('metrics', {
        parent: 'admin',
        url: '/metrics',
        data: {
          authorities: ['ROLE_ADMIN'],
          pageTitle: 'metrics.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/administrationmanagement/metrics/' +
            'metrics.html.tmpl',
            controller: 'MetricsController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
              function($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('metrics');
                return $translate.refresh();
              }]
        }
      });
    });
