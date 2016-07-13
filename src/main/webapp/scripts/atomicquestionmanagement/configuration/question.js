'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/questions/{id}',
        data: {
          authorities: [],
          pageTitle: 'metadatamanagementApp.question.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/atomicquestionmanagement/views/' +
              'question-detail.html.tmpl',
            controller: 'QuestionDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variable'); // should be changed
              return $translate.refresh();
            }
          ],
          entity: ['$stateParams', 'AtomicQuestionResource',
            function($stateParams, AtomicQuestionResource) {
              return AtomicQuestionResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
