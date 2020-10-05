'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/publications/', '/de/error');
    $urlRouterProvider.when('/en/publications/', '/en/error');
    $stateProvider
      .state('relatedPublicationDetail', {
        parent: 'site',
        url: '/publications/{id}?{version}{query}{page}' +
          '{size}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'id': {
            dynamic: true
          },
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
            'LocationSimplifier', function($stateParams,
              RelatedPublicationSearchService, LocationSimplifier) {
              var excludedAttributes = ['nested*','variables', 'dataSets',
                'surveys','dataPackages','questions', 'instruments'];
              var id = LocationSimplifier.ensureDollarSign($stateParams.id);
              return RelatedPublicationSearchService.findOneById(
                id, null, excludedAttributes);
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
