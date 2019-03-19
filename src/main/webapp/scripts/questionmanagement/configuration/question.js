'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/questions/', '/de/error');
    $urlRouterProvider.when('/en/questions/', '/en/error');
    $stateProvider
      .state('questionDetail', {
        parent: 'site',
        url: '/questions/{id}?{version}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
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
          entity: ['$stateParams', 'QuestionSearchService', 'Principal',
          function($stateParams, QuestionSearchService, Principal) {
              if (Principal.loginName()) {
                return QuestionSearchService.findOneById($stateParams.id);
              } else {
                return QuestionSearchService.findShadowByIdAndVersion(
                  $stateParams.id, $stateParams.version);
              }
            }
          ]
        }
      });
  });
