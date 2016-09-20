'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('relatedPublicationDetail', {
        parent: 'site',
        url: '/related-publications/{id}',
        data: {
          authorities: [],
          pageTitle: 'related-publication-management.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/relatedpublicationmanagement/views/' +
              'related-publication-detail.html.tmpl',
            controller: 'RelatedPublicationDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart(
                'relatedPublication.management');
            }
          ]
        },
      });
  });
