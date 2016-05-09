'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('datapackage', {
        parent: 'site',
        url: '/datapackage',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.datapackage.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datapackagemanagement/' +
              'views/datapackage.html.tmpl'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('pagination');
              $translatePartialLoader.addPart('global');
              $translatePartialLoader.addPart('datapackage');
              return $translate.refresh();
            }
          ]
        }
      });
  });
