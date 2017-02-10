'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/questions/', '/de/error');
    $urlRouterProvider.when('/en/questions/', '/en/error');
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/studies/{projectId}/instruments/{instrumentNumber}' +
          '/questions/{questionNumber}',
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
          'QuestionIdBuilderService', function($stateParams, QuestionResource,
            QuestionIdBuilderService) {
              return QuestionResource.get({
                id: QuestionIdBuilderService.buildQuestionId(
                  $stateParams.projectId,
                  $stateParams.instrumentNumber,
                  $stateParams.questionNumber)
              });
            }
          ]
        }
      });
  });
