'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/studies/', '/de/error');
    $urlRouterProvider.when('/en/studies/', '/en/error');
    $stateProvider
      .state('studyDetail', {
        parent: 'site',
        url: '/studies/{id}?{search-result-index}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/studymanagement/views/' +
              'study-detail.html.tmpl',
            controller: 'StudyDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'StudySearchService',
            function($stateParams, StudySearchService) {
              return StudySearchService.findOneById($stateParams.id);
            }
          ]
        },
      });
  });
