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
            templateUrl: 'scripts/app/entities/variable/variables.html',
            controller: 'VariablesController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variablesearch');
              $translatePartialLoader.addPart('pagination');
              $translatePartialLoader.addPart('variable');
              $translatePartialLoader.addPart('dataType');
              $translatePartialLoader.addPart('scaleLevel');
              $translatePartialLoader.addPart('global');
              return $translate.refresh();
            }
          ]
        },
        reloadOnSearch: false
      })
      .state('variable.detail', {
        parent: 'entity',
        url: '/variable/{fdzId}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.variable.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/variable/variable-detail.html',
            controller: 'VariableDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variable');
              $translatePartialLoader.addPart('dataType');
              $translatePartialLoader.addPart('scaleLevel');
              return $translate.refresh();
            }
          ],
          entity: ['$stateParams', 'Variable', function($stateParams,
            Variable) {
            return Variable.findOneByFdzId({
              fdzId: $stateParams.fdzId
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
        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams,
          $state, $uibModal) {
          $uibModal.open({
            templateUrl: 'scripts/app/entities/variable/variable-dialog.html',
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
        url: '/{fdzId}/edit',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams,
          $state, $uibModal) {
          $uibModal.open({
            templateUrl: 'scripts/app/entities/variable/variable-dialog.html',
            controller: 'VariableDialogController',
            size: 'lg',
            resolve: {
              entity: ['Variable', function(Variable) {
                return Variable.findOneByFdzId({
                  fdzId: $stateParams.fdzId
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
