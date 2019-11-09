'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/publications/', '/de/error');
    $urlRouterProvider.when('/en/publications/', '/en/error');
    $stateProvider
      .state('relatedPublicationDetail', {
        parent: 'site',
        url: '/publications/{id}?{version}{query}{related_publications}{page}{size}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
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
        }
      });

    $stateProvider
      .state('publicationAssignment', {
        parent: 'site',
        url: '/publications/assign',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/relatedpublicationmanagement/views/' +
              'publicationAssignment.html.tmpl',
            controller: 'PublicationAssignmentController',
            controllerAs: 'ctrl'
          }
        }
      });
  });
