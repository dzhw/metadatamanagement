'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('survey', {
        parent: 'site',
        url: '/surveys',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.survey.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/surveymanagement/views/survey.html.tmpl',
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
      .state('survey.new', {
        parent: 'survey',
        url: '/new',
        data: {
          authorities: ['ROLE_USER'],
        },
        onEnter: ['$stateParams', '$state', '$uibModal', 'SurveyResource',
        function($stateParams, $state, $uibModal, SurveyResource) {
          $uibModal.open({
            templateUrl:
            'scripts/surveymanagement/views/survey-dialog.html.tmpl',
            controller: 'SurveyDialogController',
            size: 'lg',
            resolve: {
              entity: function() {
                return new SurveyResource();
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
      .state('survey.detail', {
        parent: 'site',
        url: '/surveys/{id}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.survey.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/surveymanagement/views/' +
            'survey-detail.html.tmpl',
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
          entity: ['$stateParams', 'SurveyResource',
            function($stateParams, SurveyResource) {
              return SurveyResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
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
               'scripts/surveymanagement/views/survey-dialog.html.tmpl',
              controller: 'SurveyDialogController',
              size: 'lg',
              resolve: {
                entity: ['SurveyResource', function(SurveyResource) {
                  return SurveyResource.get({
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
                'scripts/surveymanagement/views/survey-delete-dialog.html.tmpl',
              controller: 'SurveyDeleteController',
              size: 'md',
              resolve: {
                entity: ['SurveyResource', function(SurveyResource) {
                  return SurveyResource.get({
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
