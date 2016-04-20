'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('dataSet', {
        parent: 'entity',
        url: '/data-sets',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.dataSet.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/dataSets.html.tmpl',
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
