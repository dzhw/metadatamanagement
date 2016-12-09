'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/studies/', '/de/error');
    $urlRouterProvider.when('/en/studies/', '/en/error');
    $stateProvider
      .state('studyDetail', {
        parent: 'site',
        url: '/studies/{id}',
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
          entity: ['$stateParams', 'StudyResource',
            function($stateParams, StudyResource) {
              return StudyResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
