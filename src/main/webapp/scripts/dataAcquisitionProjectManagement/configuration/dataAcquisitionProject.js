'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('dataAcquisitionProject', {
        parent: 'entity',
        url: '/dataAcquisitionProjects',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.dataAcquisitionProject.home.title'
        },
        views: {
          'content@': {
            templateUrl: '/scripts/dataAcquisitionProjectManagement/views/' +
              'dataAcquisitionProject.html.tmpl',
            controller: 'DataAcquisitionProjectController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('dataAcquisitionProject');
              $translatePartialLoader.addPart('global');
              return $translate.refresh();
            }
          ]
        }
      })
      .state('dataAcquisitionProject.detail', {
        parent: 'entity',
        url: '/dataAcquisitionProject/{id}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.dataAcquisitionProject.detail.title'
        },
        views: {
          'content@': {
            templateUrl: '/scripts/dataAcquisitionProjectManagement/views/' +
              'dataAcquisitionProject-detail.html.tmpl',
            controller: 'DataAcquisitionProjectDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate',
            '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('dataAcquisitionProject');
              $translatePartialLoader.addPart('pager');
              $translatePartialLoader.addPart('survey');
              $translatePartialLoader.addPart('dataSet');
              $translatePartialLoader.addPart('variable');
              $translatePartialLoader.addPart('customModal');
              $translatePartialLoader.addPart('atomicQuestion');
              return $translate.refresh();
            }
          ],
          entity: ['$stateParams', 'DataAcquisitionProject',
            function($stateParams, DataAcquisitionProject) {
              return DataAcquisitionProject.get({
                id: $stateParams.id
              });
            }
          ]
        }
      })
      .state('dataAcquisitionProject.new', {
        parent: 'dataAcquisitionProject',
        url: '/new',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl: '/scripts/dataAcquisitionProjectManagement/views/' +
                'dataAcquisitionProject-dialog.html.tmpl',
              controller: 'DataAcquisitionProjectDialogController',
              size: 'lg',
              resolve: {
                entity: ['DataAcquisitionProject', function(
                  DataAcquisitionProject) {
                  return new DataAcquisitionProject();
                }],
                isCreateMode: true
              }
            }).result.then(function() {
              $state.go('dataAcquisitionProject', null, {
                reload: true
              });
            }, function() {
              $state.go('dataAcquisitionProject');
            });
          }
        ]
      })
      .state('dataAcquisitionProject.edit', {
        parent: 'dataAcquisitionProject',
        url: '/{id}/edit',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl: '/scripts/dataAcquisitionProjectManagement/views/' +
                'dataAcquisitionProject-dialog.html.tmpl',
              controller: 'DataAcquisitionProjectDialogController',
              size: 'lg',
              resolve: {
                entity: ['DataAcquisitionProject', function(
                  DataAcquisitionProject) {
                  return DataAcquisitionProject.get({
                    id: $stateParams.id
                  });
                }],
                isCreateMode: false
              }
            }).result.then(function() {
              $state.go('dataAcquisitionProject', null, {
                reload: true
              });
            }, function() {
              $state.go('^');
            });
          }
        ]
      })
      .state('dataAcquisitionProject.delete', {
        parent: 'dataAcquisitionProject',
        url: '/{id}/delete',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl: '/scripts/dataAcquisitionProjectManagement/views/' +
                'dataAcquisitionProject-delete-dialog.html.tmpl',
              controller: 'DataAcquisitionProjectDeleteController',
              size: 'md',
              resolve: {
                entity: ['DataAcquisitionProject', function(
                  DataAcquisitionProject) {
                  return DataAcquisitionProject.get({
                    id: $stateParams.id
                  });
                }]
              }
            }).result.then(function() {
              $state.go('dataAcquisitionProject', null, {
                reload: true
              });
            }, function() {
              $state.go('^');
            });
          }
        ]
      });
  });

/*
 .state('surveylist' ,{
        url: '/dataAcquisitionProject/{id}',
        parent: 'dataAcquisitionProject.detail',
        views: {
          'surveylist': {
            templateUrl: 'scripts/app/entities/survey/' +
            'surveys.list.html.tmpl',
            controller: 'SurveysListController'
          }
        }
      })
*/