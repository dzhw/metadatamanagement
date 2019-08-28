'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/variables/', '/de/error');
    $urlRouterProvider.when('/en/variables/', '/en/error');
    $stateProvider
      .state('variableDetail', {
        parent: 'site',
        url: '/variables/{id}?{version}',
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-detail.html.tmpl',
            controller: 'VariableDetailController'
          }
        },
        resolve: {
          entity: ['$stateParams', 'VariableSearchService', 'Principal',
            function($stateParams, VariableSearchService, Principal) {
              if (Principal.loginName() && !$stateParams.version) {
                return VariableSearchService.findOneById($stateParams.id);
              } else {
                return VariableSearchService.findShadowByIdAndVersion(
                  $stateParams.id, $stateParams.version);
              }
            }
          ]
        }
      });
  });
