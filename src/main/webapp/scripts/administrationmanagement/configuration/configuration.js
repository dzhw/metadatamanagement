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
            templateUrl:
             'scripts/administrationmanagement/configuration/' +
             'configuration.html.tmpl',
            controller: 'ConfigurationController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
              function($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('configuration');
                return $translate.refresh();
              }]
        }
      });
    });
