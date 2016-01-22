'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('survey', {
        parent: 'entity',
        url: '/surveys',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.survey.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/survey/surveys.html.tmpl',
            controller: 'SurveyController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate',
            '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('survey');
              $translatePartialLoader.addPart('global');
              return $translate.refresh();
            }
          ]
        }
      })
      .state('survey.detail', {
        parent: 'entity',
        url: '/survey/{id}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.survey.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/survey/survey-detail.html.tmpl',
            controller: 'SurveyDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate',
            '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('survey');
              return $translate.refresh();
            }
          ],
          entity: ['$stateParams', 'Survey',
            function($stateParams, Survey) {
              return Survey.get({
                id: $stateParams.id
              });
            }
          ]
        }
      })
      .state('survey.new', {
        parent: 'survey',
        url: '/new',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal', 'Survey',
          function($stateParams, $state, $uibModal, Survey) {
            $uibModal.open({
              templateUrl:
               'scripts/app/entities/survey/survey-dialog.html.tmpl',
              controller: 'SurveyDialogController',
              size: 'lg',
              resolve: {
                entity: function() {
                  return new Survey();
                },
                isCreateMode: true
              }
            }).result.then(function() {
              $state.go('survey', null, {
                reload: true
              });
            }, function() {
              $state.go('survey');
            });
          }
        ]
      })
      .state('survey.edit', {
        parent: 'survey',
        url: '/{id}/edit',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl:
               'scripts/app/entities/survey/survey-dialog.html.tmpl',
              controller: 'SurveyDialogController',
              size: 'lg',
              resolve: {
                entity: ['Survey', function(Survey) {
                  return Survey.get({
                    id: $stateParams.id
                  });
                }],
                isCreateMode: false
              }
            }).result.then(function() {
              $state.go('survey', null, {
                reload: true
              });
            }, function() {
              $state.go('^');
            });
          }
        ]
      })
      .state('survey.delete', {
        parent: 'survey',
        url: '/{id}/delete',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl:
                'scripts/app/entities/survey/survey-delete-dialog.html.tmpl',
              controller: 'SurveyDeleteController',
              size: 'md',
              resolve: {
                entity: ['Survey', function(Survey) {
                  return Survey.get({
                    id: $stateParams.id
                  });
                }]
              }
            }).result.then(function() {
              $state.go('survey', null, {
                reload: true
              });
            }, function() {
              $state.go('^');
            });
          }
        ]
      });
  });
