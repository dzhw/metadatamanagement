'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/publications/', '/de/error');
    $urlRouterProvider.when('/en/publications/', '/en/error');
    $stateProvider
      .state('relatedPublicationDetail', {
        parent: 'site',
        url: '/publications/{id}?{search-result-index}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/relatedpublicationmanagement/views/' +
              'relatedPublicationDetail.html.tmpl',
            controller: 'RelatedPublicationDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'RelatedPublicationSearchService',
            function($stateParams, RelatedPublicationSearchService) {
              return RelatedPublicationSearchService.findOneById(
                $stateParams.id);
            }
          ]
        },
      });
  });
