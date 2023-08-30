'use strict';

angular.module('metadatamanagementApp')
  .config([
  '$stateProvider',
  '$urlRouterProvider',
function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(DataSetSearchService,
      SimpleMessageToastService, id, version, excludes) {
        var loadLatestShadowCopyFallback = function() {
          return DataSetSearchService.findShadowByIdAndVersion(id, null,
            excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'data-set-management.detail.not-found', {id: id});
              return null;
            }
          });
        };
        return DataSetSearchService.findShadowByIdAndVersion(id, version,
          excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              return loadLatestShadowCopyFallback();
            }
          });
      };
    var stateName = 'dataSetDetail';
    $urlRouterProvider.when('/de/data-sets/', '/de/error');
    $urlRouterProvider.when('/en/data-sets/', '/en/error');
    $stateProvider
      .state(stateName, {
        parent: 'site',
        url: '/data-sets/{id}?{page}{query}{size}{type}{version}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/' +
              'dataSet-detail.html.tmpl',
            controller: 'DataSetDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'DataSetSearchService', 'Principal', '$q',
            'SimpleMessageToastService', 'LocationSimplifier', '$state',
            function($stateParams, DataSetSearchService, Principal, $q,
              SimpleMessageToastService, LocationSimplifier, $state) {
              return LocationSimplifier.removeDollarSign($state, $stateParams,
                stateName).then(function() {
                  var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                  var excludedAttributes = ['nested*','variables','questions',
                  'instruments', 'relatedPublications','concepts'];
                  if (Principal.loginName() && !$stateParams.version) {
                    return DataSetSearchService.findOneById(id, null,
                      excludedAttributes);
                  } else {
                    var deferred = $q.defer();
                    loadShadowCopy(DataSetSearchService,
                      SimpleMessageToastService, id,
                      $stateParams.version, excludedAttributes)
                      .then(deferred.resolve, deferred.reject);
                    return deferred;
                  }
                }).catch($q.defer);
            }
          ]
        }
      });

    $stateProvider
      .state('dataSetEdit', {
        parent: 'site',
        url: '/data-sets/{id}/edit',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/' +
              'data-set-edit-or-create.html.tmpl',
            controller: 'DataSetEditOrCreateController',
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
          entity: ['$stateParams', 'DataSetResource',
            function($stateParams, DataSetResource) {
              return DataSetResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });

    $stateProvider
      .state('dataSetCreate', {
        parent: 'site',
        url: '/data-sets/new',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datasetmanagement/views/' +
              'data-set-edit-or-create.html.tmpl',
            controller: 'DataSetEditOrCreateController',
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
  }]);
