'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('dataSetDetail', {
        parent: 'site',
        url: '/data-sets/{id}',
        data: {
          authorities: [],
          pageTitle: 'metadatamanagementApp.dataset.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/' +
              'dataSet-detail.html.tmpl',
            controller: 'DataSetDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variable'); // should be changed
              return $translate.refresh();
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
