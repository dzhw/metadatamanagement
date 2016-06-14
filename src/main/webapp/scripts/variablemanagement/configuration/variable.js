'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('variableDetail', {
        parent: 'site',
        url: '/variables/{id}',
        data: {
          authorities: ['ROLE_USER'],
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
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variable');
              return $translate.refresh();
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
