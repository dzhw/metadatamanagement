'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('dataSet', {
        parent: 'entity',
        url: '/dataSets',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.dataSet.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/dataSet/dataSets.html.tmpl',
            controller: 'DataSetController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate',
            '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('dataSet');
              $translatePartialLoader.addPart('dataAcquisitionProject');
              $translatePartialLoader.addPart('global');
              return $translate.refresh();
            }
          ]
        }
      });
  });
