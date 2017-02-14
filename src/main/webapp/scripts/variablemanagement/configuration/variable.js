'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/variables/', '/de/error');
    $urlRouterProvider.when('/en/variables/', '/en/error');
    $stateProvider
      .state('variableDetail', {
        parent: 'site',
        url: '/studies/{projectId}/data-sets/' +
        '{dataSetNumber}/variables/{variableName}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-detail.html.tmpl',
            controller: 'VariableDetailController'
          }
        },
        resolve: {
          entity: ['$stateParams', 'VariableResource',
          'VariableIdBuilderService',
            function($stateParams, VariableResource, VariableIdBuilderService) {
              return VariableResource.get({
                id: VariableIdBuilderService
                .buildVariableId($stateParams.projectId,
                  $stateParams.dataSetNumber, $stateParams.variableName)
              });
            }
          ]
        },
      });
  });
