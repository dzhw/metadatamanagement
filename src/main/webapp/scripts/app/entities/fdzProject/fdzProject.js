'use strict';

angular.module('metadatamanagementApp')
    .config(function($stateProvider) {
      $stateProvider
          .state('fdzProject', {
            parent: 'entity',
            url: '/fdzProjects',
            data: {
              authorities: ['ROLE_USER'],
              pageTitle: 'metadatamanagementApp.fdzProject.home.title'
            },
            views: {
              'content@': {
                templateUrl: 'scripts/app/entities/fdzProject/fdzProjects.html',
                controller: 'FdzProjectController'
              }
            },
            resolve: {
              translatePartialLoader: ['$translate', '$translatePartialLoader',
              function($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('fdzProject');
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
              }]
            }
          })
            .state('fdzProject.detail', {
              parent: 'entity',
              url: '/fdzProject/{name}',
              data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'metadatamanagementApp.fdzProject.detail.title'
              },
              views: {
                'content@': {
                  templateUrl:
                    'scripts/app/entities/fdzProject/fdzProject-detail.html',
                  controller: 'FdzProjectDetailController'
                }
              },
              resolve: {
                translatePartialLoader: ['$translate',
                  '$translatePartialLoader',
                  function($translate, $translatePartialLoader) {
                  $translatePartialLoader.addPart('fdzProject');
                  return $translate.refresh();
                }],
                entity: ['$stateParams', 'FdzProject',
                  function($stateParams, FdzProject) {
                  return FdzProject.findOneByName({name: $stateParams.name});
                }]
              }
            })
            .state('fdzProject.new', {
              parent: 'fdzProject',
              url: '/new',
              data: {
                authorities: ['ROLE_USER'],
              },
              onEnter: ['$stateParams', '$state', '$uibModal',
                function($stateParams, $state, $uibModal) {
                $uibModal.open({
                  templateUrl:
                    'scripts/app/entities/fdzProject/fdzProject-dialog.html',
                  controller: 'FdzProjectDialogController',
                  size: 'lg',
                  resolve: {
                    entity: function() {
                      return {
                        name: null,
                        sufDoi: null,
                        cufDoi: null,
                        id: null
                      };
                    },
                    isCreateMode: true
                  }
                }).result.then(function() {
                  $state.go('fdzProject', null, {reload: true});
                }, function() {
                  $state.go('fdzProject');
                });
              }]
            })
            .state('fdzProject.edit', {
              parent: 'fdzProject',
              url: '/{name}/edit',
              data: {
                authorities: ['ROLE_USER'],
              },
              onEnter: ['$stateParams', '$state', '$uibModal',
                function($stateParams, $state, $uibModal) {
                $uibModal.open({
                  templateUrl:
                  'scripts/app/entities/fdzProject/fdzProject-dialog.html',
                  controller: 'FdzProjectDialogController',
                  size: 'lg',
                  resolve: {
                    entity: ['FdzProject', function(FdzProject) {
                      return FdzProject.findOneByName(
                        {name: $stateParams.name});
                    }],
                    isCreateMode: false
                  }
                }).result.then(function() {
                  $state.go('fdzProject', null, {reload: true});
                }, function() {
                  $state.go('^');
                });
              }]
            })
            .state('fdzProject.delete', {
              parent: 'fdzProject',
              url: '/{name}/delete',
              data: {
                authorities: ['ROLE_USER'],
              },
              onEnter: ['$stateParams', '$state', '$uibModal',
                function($stateParams, $state, $uibModal) {
                $uibModal.open({
                  templateUrl:
                  'scripts/app/entities/fdzProject/' +
                  'fdzProject-delete-dialog.html',
                  controller: 'FdzProjectDeleteController',
                  size: 'md',
                  resolve: {
                    entity: ['FdzProject', function(FdzProject) {
                      return FdzProject.findOneByName({name:
                        $stateParams.name});
                    }]
                  }
                }).result.then(function() {
                  $state.go('fdzProject', null, {reload: true});
                }, function() {
                  $state.go('^');
                });
              }]
            });
    });
