'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/data-sets/', '/de/error');
    $urlRouterProvider.when('/en/data-sets/', '/en/error');
    $stateProvider
      .state('data-setDetail', {
        parent: 'site',
        url: '/studies/{projectId}/data-sets/{dataSetNumber}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/' +
              'dataSet-detail.html.tmpl',
            controller: 'DataSetDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'DataSetResource', 'DataSetIdBuilderService',
            function($stateParams, DataSetResource, DataSetIdBuilderService) {
              return DataSetResource.get({
                id: DataSetIdBuilderService
                .buildDataSetId($stateParams.projectId,
                  $stateParams.dataSetNumber)
              });
            }
          ]
        },
      });
  });
