'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/instruments/', '/de/error');
    $urlRouterProvider.when('/en/instruments/', '/en/error');
    $stateProvider
      .state('instrumentDetail', {
        parent: 'site',
        url: '/instruments/{id}?{search-result-index}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/instrumentmanagement/views/' +
              'instrument-detail.html.tmpl',
            controller: 'InstrumentDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'InstrumentSearchService',
            function($stateParams, InstrumentSearchService) {
              return InstrumentSearchService.findOneById($stateParams.id);
            }
          ]
        },
      });
  });
