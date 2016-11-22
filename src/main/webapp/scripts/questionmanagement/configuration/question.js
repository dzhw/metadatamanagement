'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/questions/{id}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/questionmanagement/views/' +
              'question-detail.html.tmpl',
            controller: 'QuestionDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('global');
              $translatePartialLoader.addPart('question.management');
              $translatePartialLoader.addPart('variable.management');
              $translatePartialLoader.addPart('study.management');
              $translatePartialLoader.addPart('notepad.management');
              $translatePartialLoader.addPart('instrument.management');
              $translatePartialLoader.addPart(
                'relatedPublication.management');
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
