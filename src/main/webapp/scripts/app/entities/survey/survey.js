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
            templateUrl: 'scripts/app/entities/survey/surveys.html',
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
        url: '/survey/{fdzId}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.survey.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/survey/survey-detail.html',
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
              return Survey.findOneByFdzId({
                fdzId: $stateParams.fdzId
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
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl: 'scripts/app/entities/survey/survey-dialog.html',
              controller: 'SurveyDialogController',
              size: 'lg',
              resolve: {
                entity: function() {
                  return {
                    title: null,
                    fieldPeriod: null,
                    fdzProjectName: null,
                    id: null
                  };
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
        url: '/{fdzId}/edit',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl: 'scripts/app/entities/survey/survey-dialog.html',
              controller: 'SurveyDialogController',
              size: 'lg',
              resolve: {
                entity: ['Survey', function(Survey) {
                  return Survey.findOneByFdzId({
                    fdzId: $stateParams.fdzId
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
        url: '/{fdzId}/delete',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal',
          function($stateParams, $state, $uibModal) {
            $uibModal.open({
              templateUrl:
                'scripts/app/entities/survey/survey-delete-dialog.html',
              controller: 'SurveyDeleteController',
              size: 'md',
              resolve: {
                entity: ['Survey', function(Survey) {
                  return Survey.findOneByFdzId({
                    fdzId: $stateParams.fdzId
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
