'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
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
