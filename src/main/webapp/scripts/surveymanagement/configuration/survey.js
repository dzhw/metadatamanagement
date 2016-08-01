'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('surveyDetail', {
        parent: 'site',
        url: '/surveys/{id}',
        data: {
          authorities: [],
          pageTitle: 'Erhebung'
          // should be a i18n string
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
              $translatePartialLoader.addPart('variable'); // should be changed
              $translatePartialLoader.addPart('survey'); // should be changed
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
