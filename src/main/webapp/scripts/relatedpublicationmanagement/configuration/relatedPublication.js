'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/publications/', '/de/error');
    $urlRouterProvider.when('/en/publications/', '/en/error');
    $stateProvider
      .state('relatedPublicationDetail', {
        parent: 'site',
        url: '/publications/{id}',
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
        },
        onEnter: function($rootScope, $timeout) {
          $timeout(function() {
            $rootScope.$broadcast('domain-object-editing-started');
          }, 500);
        },
        onExit: function($rootScope, $timeout) {
          $timeout(function() {
            $rootScope.$broadcast('domain-object-editing-stopped');
          }, 500);
        }
      });
  });
