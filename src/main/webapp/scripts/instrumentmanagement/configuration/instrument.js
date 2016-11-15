'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('instrumentDetail', {
        parent: 'site',
        url: '/instruments/{id}',
        data: {
          authorities: [],
          pageTitle: 'instrument-management.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/instrumentmanagement/views/' +
              'instrument-detail.html.tmpl',
            controller: 'InstrumentDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('instrument.management');
              $translatePartialLoader.addPart('question.management');
              $translatePartialLoader.addPart('survey.management');
              $translatePartialLoader.addPart('study.management');
            }
          ],
          entity: ['$stateParams', 'InstrumentResource',
            function($stateParams, InstrumentResource) {
              return InstrumentResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
