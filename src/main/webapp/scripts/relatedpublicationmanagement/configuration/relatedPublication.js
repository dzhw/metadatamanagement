'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var stateName = 'relatedPublicationDetail';
    $urlRouterProvider.when('/de/publications/', '/de/error');
    $urlRouterProvider.when('/en/publications/', '/en/error');
    $stateProvider
      .state(stateName, {
        parent: 'site',
        url: '/publications/{id}?{version}{query}{page}' +
          '{size}',
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
            'LocationSimplifier', '$state', '$q', function($stateParams,
              RelatedPublicationSearchService, LocationSimplifier, $state, $q) {
              return LocationSimplifier.removeDollarSign($state, $stateParams,
                stateName).then(function() {
                  var excludedAttributes = ['nested*','variables', 'dataSets',
                  'surveys','dataPackages','questions', 'instruments'];
                  var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                  return RelatedPublicationSearchService.findOneById(
                    id, null, excludedAttributes);
                }
              ).catch($q.defer);
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
