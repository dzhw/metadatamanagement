'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(InstrumentSearchService,
      SimpleMessageToastService, id, version) {
        var loadLatestShadowCopyFallback = function() {
          return InstrumentSearchService.findShadowByIdAndVersion(id, null)
          .promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'instrument-management.detail.not-found', {id: id});
              return null;
            }
          });
        };
        return InstrumentSearchService.findShadowByIdAndVersion(id, version)
          .promise.then(function(result) {
            if (result) {
              return result;
            } else {
              return loadLatestShadowCopyFallback();
            }
          });
      };

    $urlRouterProvider.when('/de/instruments/', '/de/error');
    $urlRouterProvider.when('/en/instruments/', '/en/error');
    $stateProvider
      .state('instrumentDetail', {
        parent: 'site',
        url: '/instruments/{id}?{version}{query}{instrument}{page}{size}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
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
            'SimpleMessageToastService', '$q',
            function($stateParams, InstrumentSearchService, Principal,
              SimpleMessageToastService, $q) {
              if (Principal.loginName() && !$stateParams.version) {
                return InstrumentSearchService.findOneById($stateParams.id);
              } else {
                var deferred = $q.defer();
                loadShadowCopy(InstrumentSearchService,
                  SimpleMessageToastService, $stateParams.id,
                  $stateParams.version).then(deferred.resolve, deferred.reject);
                return deferred;
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
