'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('search', {
        parent: 'site',
        url: '/',
        data: {
          authorities: [],
          pageTitle: 'global.menu.search.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/searchmanagement/views/search.html.tmpl',
            controller: 'SearchController'
          }
        }
      });
    });
