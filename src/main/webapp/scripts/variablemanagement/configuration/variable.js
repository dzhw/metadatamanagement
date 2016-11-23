'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/variables/', '/de/error');
    $urlRouterProvider.when('/en/variables/', '/en/error');
    $stateProvider
      .state('variableDetail', {
        parent: 'site',
        url: '/variables/{id}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-detail.html.tmpl',
            controller: 'VariableDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('variable.management');
              $translatePartialLoader.addPart('question.management');
              $translatePartialLoader.addPart('survey.management');
              $translatePartialLoader.addPart('dataSet.management');
              $translatePartialLoader.addPart('study.management');
              $translatePartialLoader.addPart('notepad.management');
              $translatePartialLoader.addPart('instrument.management');
              $translatePartialLoader.addPart(
                'relatedPublication.management');
            }
          ],
          entity: ['$stateParams', 'VariableResource',
            function($stateParams, VariableResource) {
              return VariableResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
