'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/instruments/', '/de/error');
    $urlRouterProvider.when('/en/instruments/', '/en/error');
    $stateProvider
      .state('instrumentDetail', {
        parent: 'site',
        url: '/studies/{projectId}/instruments/{instrumentNumber}',
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
          entity: ['$stateParams', 'InstrumentResource',
          'InstrumentIdBuilderService', function($stateParams,
            InstrumentResource, InstrumentIdBuilderService) {
              return InstrumentResource.get({
                id: InstrumentIdBuilderService.buildInstrumentId($stateParams.
                  projectId, $stateParams.instrumentNumber)
              });
            }
          ]
        },
      });
  });
