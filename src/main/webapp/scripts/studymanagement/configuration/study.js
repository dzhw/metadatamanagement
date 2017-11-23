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

      $stateProvider
        .state('studyEdit', {
          parent: 'site',
          url: '/studies/{id}/edit',
          data: {
            authorities: ['ROLE_PUBLISHER']
          },
          views: {
            'content@': {
              templateUrl: 'scripts/studymanagement/views/' +
                'study-edit-or-create.html.tmpl',
              controller: 'StudyEditOrCreateController',
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

        $stateProvider
          .state('studyCreate', {
            parent: 'site',
            url: '/studies/new',
            data: {
              authorities: ['ROLE_PUBLISHER']
            },
            views: {
              'content@': {
                templateUrl: 'scripts/studymanagement/views/' +
                  'study-edit-or-create.html.tmpl',
                controller: 'StudyEditOrCreateController',
                controllerAs: 'ctrl'
              }
            },
            resolve: {
              entity: function() {
                return null;
              }
            },
          });
  });
