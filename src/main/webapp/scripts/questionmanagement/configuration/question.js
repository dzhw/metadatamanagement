'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/questions/{id}',
        data: {
          authorities: [],
          pageTitle: 'question-management.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/questionmanagement/views/' +
              'question-detail.html.tmpl',
            controller: 'QuestionDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('question.management');
              $translatePartialLoader.addPart('variable.management');
              $translatePartialLoader.addPart('study.management');
              $translatePartialLoader.addPart('notepad.management');
              $translatePartialLoader.addPart('instrument.management');
            }
          ],
          entity: ['$stateParams', 'QuestionResource',
            function($stateParams, QuestionResource) {
              return QuestionResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });
  });
