'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/surveys/', '/de/error');
    $urlRouterProvider.when('/en/surveys/', '/en/error');
    $stateProvider
      .state('surveyDetail', {
        parent: 'site',
        url: '/studies/{projectId}/surveys/{surveyNumber}',
        data: {
          authorities: []
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
          entity: ['$stateParams', 'SurveyResource',
            function($stateParams, SurveyResource) {
              return SurveyResource.get({
                id: $stateParams.projectId + '-sy' +
                  $stateParams.surveyNumber
              });
            }
          ]
        },
      });
  });
