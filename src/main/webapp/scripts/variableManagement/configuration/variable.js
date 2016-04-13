'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('variable', {
        parent: 'entity',
        url: '/variables?{page, query}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.variable.home.title'
        },
        views: {
          'content@': {
            templateUrl: '/scripts/variableManagement/' +
            'views/variables.html.tmpl',
            controller: 'VariableController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variablesearch');
              $translatePartialLoader.addPart('pagination');
              $translatePartialLoader.addPart('variable');
              $translatePartialLoader.addPart('global');
              return $translate.refresh();
            }
          ]
        },
        reloadOnSearch: false
      })
      .state('variable.detail', {
        parent: 'entity',
        url: '/variable/{id}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.variable.detail.title'
        },
        views: {
          'content@': {
            templateUrl: '/scripts/variableManagement/views/' +
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
          entity: ['$stateParams', 'Variable', function($stateParams,
            Variable) {
            return Variable.get({
              id: $stateParams.id
            });
          }]
        },
      })
      .state('variable.new', {
        parent: 'variable',
        url: '/new',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal', function(
          $stateParams,
          $state, $uibModal) {
          $uibModal.open({
            templateUrl: '/scripts/variableManagement/views/' +
              'variable-dialog.html.tmpl',
            controller: 'VariableDialogController',
            size: 'lg',
            resolve: {
              entity: ['Variable', function(Variable) {
                return new Variable();
              }],
              isCreateMode: true
            }
          }).result.then(function() {
            $state.go('variable', null, {
              reload: true
            });
          }, function() {
            $state.go('variable');
          });
        }]
      })
      .state('variable.edit', {
        parent: 'variable',
        url: '/{id}/edit',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal', function(
          $stateParams,
          $state, $uibModal) {
          $uibModal.open({
            templateUrl: '/scripts/variableManagement/views/' +
              'variable-dialog.html.tmpl',
            controller: 'VariableDialogController',
            size: 'lg',
            resolve: {
              entity: ['Variable', function(Variable) {
                return Variable.get({
                  id: $stateParams.id
                });
              }],
              isCreateMode: false
            }
          }).result.then(function() {
            $state.go('variable', null, {
              reload: true
            });
          }, function() {
            $state.go('^');
          });
        }]
      });
  });
