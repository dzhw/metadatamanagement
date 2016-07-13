'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('atomicQuestionDetail', {
        parent: 'site',
        url: '/atomic-questions/{id}',
        data: {
          authorities: [],
          pageTitle: 'metadatamanagementApp.atomicQuestion.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/atomicquestionmanagement/views/' +
              'atomicQuestion-detail.html.tmpl',
            controller: 'AtomicQuestionDetailController'
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
