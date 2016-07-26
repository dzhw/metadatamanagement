'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('studyDetail', {
        parent: 'site',
        url: '/studies/{id}',
        data: {
          authorities: [],
          pageTitle: 'Studie'
          // should be a i18n string
        },
        views: {
          'content@': {
            templateUrl: 'scripts/studymanagement/views/' +
              'study-detail.html.tmpl',
            controller: 'StudyDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('variable'); // should be changed
              // return $translate.refresh();
            }
          ]
        },
      });
  });
