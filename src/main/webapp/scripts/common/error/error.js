'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state(
          'error',
          {
            parent: 'site',
            url: '/error',
            data: {
              authorities: [],
              pageTitle: 'error.title'
            },
            views: {
              'content@': {
                templateUrl: 'scripts/common/errornotification/error/' +
                'error.html.tmpl'
              }
            },
            resolve: {
              mainTranslatePartialLoader: ['$translate',
                  '$translatePartialLoader',
                  function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('error');
                    return $translate.refresh();
                  }]
            }
          }).state(
          'accessdenied',
          {
            parent: 'site',
            url: '/accessdenied',
            data: {
              authorities: []
            },
            views: {
              'content@': {
                templateUrl: 'scripts/common/errornotification/error/' +
                'accessdenied.html.tmpl'
              }
            },
            resolve: {
              mainTranslatePartialLoader: ['$translate',
                  '$translatePartialLoader',
                  function($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('error');
                    return $translate.refresh();
                  }]
            }
          });
    });
