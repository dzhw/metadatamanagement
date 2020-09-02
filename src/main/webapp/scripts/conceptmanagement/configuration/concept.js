'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/concepts/', '/de/error');
    $urlRouterProvider.when('/en/concepts/', '/en/error');
    $stateProvider
      .state('conceptDetail', {
        parent: 'site',
        url: '/concepts/{id}?{version}{query}{page}{size}',
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
            templateUrl: 'scripts/conceptmanagement/views/' +
              'concept-detail.html.tmpl',
            controller: 'ConceptDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'ConceptSearchService', 'LocationSimplifier',
            function($stateParams, ConceptSearchService, LocationSimplifier) {
              var excludedAttributes = ['nested*', 'studies', 'dataSets',
                'surveys','variables','questions', 'instruments'];
              var id = LocationSimplifier.ensureDollarSign($stateParams.id);
              return ConceptSearchService.findOneById(id, null,
                excludedAttributes);
            }
          ]
        }
      });

    $stateProvider
      .state('conceptEdit', {
        parent: 'site',
        url: '/concepts/{id}/edit',
        data: {
          authorities: ['ROLE_PUBLISHER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/conceptmanagement/views/' +
              'concept-edit-or-create.html.tmpl',
            controller: 'ConceptEditOrCreateController',
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
        },
        resolve: {
          entity: ['$stateParams', 'ConceptResource',
            function($stateParams, ConceptResource) {
              return ConceptResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });

    $stateProvider
      .state('conceptCreate', {
        parent: 'site',
        url: '/concepts/new',
        data: {
          authorities: ['ROLE_PUBLISHER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/conceptmanagement/views/' +
              'concept-edit-or-create.html.tmpl',
            controller: 'ConceptEditOrCreateController',
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
        },
        resolve: {
          entity: function() {
            return null;
          }
        }
      });
  });
