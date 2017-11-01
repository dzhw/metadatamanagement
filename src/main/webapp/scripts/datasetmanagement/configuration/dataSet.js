'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/data-sets/', '/de/error');
    $urlRouterProvider.when('/en/data-sets/', '/en/error');
    $stateProvider
      .state('dataSetDetail', {
        parent: 'site',
        url: '/data-sets/{id}?{search-result-index}',
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
          entity: ['$stateParams', 'DataSetSearchService',
            function($stateParams, DataSetSearchService) {
              return DataSetSearchService.findOneById($stateParams.id);
            }
          ]
        },
      });
  });
