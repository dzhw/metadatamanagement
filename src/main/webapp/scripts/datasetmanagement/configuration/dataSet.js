'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('data-setDetail', {
        parent: 'site',
        url: '/data-sets/{id}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/' +
              'dataSet-detail.html.tmpl',
            controller: 'DataSetDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('dataSet.management');
              $translatePartialLoader.addPart('variable.management');
              $translatePartialLoader.addPart('survey.management');
              $translatePartialLoader.addPart('study.management');
              $translatePartialLoader.addPart('notepad.management');
              $translatePartialLoader.addPart(
                'relatedPublication.management');
            }
          ],
          entity: ['$stateParams', 'DataSetResource',
            function($stateParams, DataSetResource) {
              return DataSetResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });
  });
