'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('dataSetDetail', {
        parent: 'site',
        url: '/data-sets/{id}',
        data: {
          authorities: [],
          //TODO should be a i18n string
          pageTitle: 'Datensatz'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/' +
              'dataSet-detail.html.tmpl',
            controller: 'DataSetDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('dataSet.management');
            }
          ],
          entity: ['$stateParams', 'DataSetResource',
            function($stateParams, DataSetResource) {
              return DataSetResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
