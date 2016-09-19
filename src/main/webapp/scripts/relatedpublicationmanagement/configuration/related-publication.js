'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('relatedPublicationDetail', {
        parent: 'site',
        url: '/related-publications/{id}',
        data: {
          authorities: [],
          //TODO replace with i18n.
          pageTitle: 'Publikation'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/relatedpublicationmanagement/views/' +
              'related-publication-detail.html.tmpl',
            controller: 'RelatedPublicationDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              //TODO replace. just fake at the moment
              $translatePartialLoader.addPart(
                'relatedPublication.management');
              $translatePartialLoader.addPart('study.management');
              $translatePartialLoader.addPart('question.management');
              $translatePartialLoader.addPart('variable.management');
            }
          ]
        },
      });
  });
