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
            }
          ],
          entity: ['$stateParams', 'QuestionResource',
            function($stateParams, QuestionResource) {
              return QuestionResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
