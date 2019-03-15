'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/instruments/', '/de/error');
    $urlRouterProvider.when('/en/instruments/', '/en/error');
    $stateProvider
      .state('instrumentDetail', {
        parent: 'site',
        url: '/instruments/{id}?{search-result-index},{version}',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/instrumentmanagement/views/' +
              'instrument-detail.html.tmpl',
            controller: 'InstrumentDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'InstrumentSearchService', 'Principal',
            function($stateParams, InstrumentSearchService, Principal) {
              if (Principal.loginName()) {
                return InstrumentSearchService.findOneById($stateParams.id);
              } else {
                return InstrumentSearchService.findShadowByIdAndVersion(
                  $stateParams.id, $stateParams.version);
              }
            }
          ]
        }
      });

    $stateProvider
      .state('instrumentEdit', {
        parent: 'site',
        url: '/instruments/{id}/edit',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/instrumentmanagement/views/' +
              'instrument-edit-or-create.html.tmpl',
            controller: 'InstrumentEditOrCreateController',
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
          entity: ['$stateParams', 'InstrumentResource',
            function($stateParams, InstrumentResource) {
              return InstrumentResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });

    $stateProvider
      .state('instrumentCreate', {
        parent: 'site',
        url: '/instruments/new',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/instrumentmanagement/views/' +
              'instrument-edit-or-create.html.tmpl',
            controller: 'InstrumentEditOrCreateController',
            controllerAs: 'ctrl'
          }
        },
        onEnter: function($rootScope, $timeout) {
          $rootScope.$broadcast('start-ignoring-404');
          $timeout(function() {
            $rootScope.$broadcast('domain-object-editing-started');
          }, 500);
        },
        onExit: function($rootScope, $timeout) {
          $rootScope.$broadcast('stop-ignoring-404');
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
