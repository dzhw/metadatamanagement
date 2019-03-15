'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/variables/', '/de/error');
    $urlRouterProvider.when('/en/variables/', '/en/error');
    $stateProvider
      .state('variableDetail', {
        parent: 'site',
        url: '/variables/{id}?{search-result-index},{version}',
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
          entity: ['$stateParams', 'VariableSearchService', 'Principal',
            function($stateParams, VariableSearchService, Principal) {
              if (Principal.loginName()) {
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
