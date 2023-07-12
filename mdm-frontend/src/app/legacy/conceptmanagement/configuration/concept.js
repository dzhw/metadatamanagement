'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var stateName = 'conceptDetail';
    $urlRouterProvider.when('/de/concepts/', '/de/error');
    $urlRouterProvider.when('/en/concepts/', '/en/error');
    $stateProvider
      .state(stateName, {
        parent: 'site',
        url: '/concepts/{id}?{page}{query}{size}{type}{version}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
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
            '$state', '$q',
            function($stateParams, ConceptSearchService, LocationSimplifier,
              $state, $q) {
              return LocationSimplifier.removeDollarSign($state, $stateParams,
                stateName).then(function() {
                  var excludedAttributes = ['nested*','dataPackages',
                  'dataSets','surveys','variables','questions','instruments'];
                  var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                  return ConceptSearchService.findOneById(id, null,
                    excludedAttributes);
                }).catch($q.defer);
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
