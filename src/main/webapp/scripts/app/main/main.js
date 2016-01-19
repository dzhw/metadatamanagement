'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('home', {
        parent: 'site',
        url: '/',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/main/main.html.tmpl',
            controller: 'MainController'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', '$translatePartialLoader',
              function($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('main');
                return $translate.refresh();
              }]
        }
      });
    });
