'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/questions/{id}',
        data: {
          authorities: [],
          //TODO should be a i18n string
          pageTitle: 'Frage'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/atomicquestionmanagement/views/' +
              'question-detail.html.tmpl',
            controller: 'QuestionDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              //should be changed
              $translatePartialLoader.addPart('question.management');
            }
          ],
          entity: ['$stateParams', 'AtomicQuestionResource',
            function($stateParams, AtomicQuestionResource) {
              return AtomicQuestionResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });
  });
