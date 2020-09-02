'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(InstrumentSearchService,
      SimpleMessageToastService, id, version, excludes) {
        var loadLatestShadowCopyFallback = function() {
          return InstrumentSearchService.findShadowByIdAndVersion(id, null,
            excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'instrument-management.detail.not-found', {id: id});
              return null;
            }
          });
        };
        return InstrumentSearchService.findShadowByIdAndVersion(id, version,
          excludes).promise.then(function(result) {
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
        url: '/instruments/{id}?{version}{query}{page}{size}',
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
            templateUrl: 'scripts/instrumentmanagement/views/' +
              'instrument-detail.html.tmpl',
            controller: 'InstrumentDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'InstrumentSearchService', 'Principal',
            'SimpleMessageToastService', '$q', 'LocationSimplifier',
            function($stateParams, InstrumentSearchService, Principal,
              SimpleMessageToastService, $q, LocationSimplifier) {
              var excludedAttributes = ['nested*','questions', 'dataSets',
                'variables','relatedPublications','concepts'];
              var id = LocationSimplifier.ensureDollarSign($stateParams.id);
              if (Principal.loginName() && !$stateParams.version) {
                return InstrumentSearchService.findOneById(id,
                  null, excludedAttributes);
              } else {
                var deferred = $q.defer();
                loadShadowCopy(InstrumentSearchService,
                  SimpleMessageToastService, id,
                  $stateParams.version, excludedAttributes)
                  .then(deferred.resolve, deferred.reject);
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
