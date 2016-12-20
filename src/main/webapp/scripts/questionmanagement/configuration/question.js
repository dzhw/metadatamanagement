'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/questions/', '/de/error');
    $urlRouterProvider.when('/en/questions/', '/en/error');
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/study/{projectId}/instruments/{instrumentNumber}/questions/{questionNumber}',
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
          entity: ['$stateParams', 'QuestionResource',
            function($stateParams, QuestionResource) {
              return QuestionResource.get({
                id: $stateParams.projectId + '-ins' +
                  $stateParams.instrumentNumber + '-' +
                  $stateParams.questionNumber
              });
            }
          ]
        }
      });
  });
