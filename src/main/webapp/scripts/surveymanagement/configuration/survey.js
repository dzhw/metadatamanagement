'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('surveyDetail', {
        parent: 'site',
        url: '/surveys/{id}',
        data: {
          authorities: [],
          pageTitle: 'survey-management.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/surveymanagement/views/' +
              'survey-detail.html.tmpl',
            controller: 'SurveyDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('relatedPublication.management');
              $translatePartialLoader.addPart('variable.management');
              $translatePartialLoader.addPart('question.management');
              $translatePartialLoader.addPart('survey.management');
              $translatePartialLoader.addPart('dataSet.management');
              $translatePartialLoader.addPart('study.management');
              $translatePartialLoader.addPart('instrument.management');
            }
          ],
          entity: ['$stateParams', 'SurveyResource',
            function($stateParams, SurveyResource) {
              return SurveyResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
