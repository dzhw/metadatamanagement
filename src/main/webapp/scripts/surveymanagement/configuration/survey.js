'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('surveyDetail', {
        parent: 'site',
        url: '/surveys/{id}',
        data: {
          authorities: [],
          //TODO should be a i18n string
          pageTitle: 'Erhebung'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/surveymanagement/views/' +
              'survey-detail.html.tmpl',
            controller: 'SurveyDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              // should be changed
              $translatePartialLoader.addPart('variable.management');
              $translatePartialLoader.addPart('survey.management');
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
