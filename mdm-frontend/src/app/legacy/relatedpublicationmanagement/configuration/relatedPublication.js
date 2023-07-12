'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var stateName = 'relatedPublicationDetail';
    $urlRouterProvider.when('/de/publications/', '/de/error');
    $urlRouterProvider.when('/en/publications/', '/en/error');
    $stateProvider
      .state(stateName, {
        parent: 'site',
        url: '/publications/{id}?{page}{query}{size}{type}{version}',
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
  });
