'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/surveys/', '/de/error');
    $urlRouterProvider.when('/en/surveys/', '/en/error');
    $stateProvider
      .state('surveyDetail', {
        parent: 'site',
        url: '/surveys/{id}?{search-result-index}',
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
          entity: ['$stateParams', 'SurveySearchService',
            function($stateParams, SurveySearchService) {
              return SurveySearchService.findOneById($stateParams.id);
            }
          ]
        },
      });
  });
