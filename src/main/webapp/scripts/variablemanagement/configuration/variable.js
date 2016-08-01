'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('variableDetail', {
        parent: 'site',
        url: '/variables/{id}',
        data: {
          authorities: [],
          pageTitle: 'metadatamanagementApp.variable.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-detail.html.tmpl',
            controller: 'VariableDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('variable');
            }
          ],
          entity: ['$stateParams', 'VariableResource',
            function($stateParams, VariableResource) {
              return VariableResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
