'use strict';

angular.module('metadatamanagementApp')
  .config([
  '$stateProvider',
  '$urlRouterProvider',
function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(DataPackageSearchService,
        SimpleMessageToastService, id, version, excludes) {
      var loadLatestShadowCopyFallback = function() {
        return DataPackageSearchService.findShadowByIdAndVersion(id, null,
          excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'data-package-management.detail.not-found', {id: id}, 5000);
              return null;
            }
          });
      };

      return DataPackageSearchService.findShadowByIdAndVersion(id, version,
        excludes).promise.then(function(result) {
          if (result) {
            return result;
          } else {
            return loadLatestShadowCopyFallback();
          }
        });
    };

    $urlRouterProvider.when('/de/data-packages/', '/de/error');
    $urlRouterProvider.when('/en/data-packages/', '/en/error');
    var stateName = 'dataPackageDetail';
    var dataPackageDetailConfig = {
      parent: 'site',
      url: '/data-packages/{id}?{access-way}{derived-variables-identifier}' +
        '{page}{query}{repeated-measurement-identifier}{size}{type}{version}',
      reloadOnSearch: false,
      data: {
        authorities: []
      },
      params: {
        'search-result-index': null
      },
      views: {
        'content@': {
          templateUrl: 'scripts/datapackagemanagement/views/' +
            'data-package-detail.html.tmpl',
          controller: 'DataPackageDetailController',
          controllerAs: 'ctrl'
        }
      },
      resolve: {
        entity: ['$q', '$stateParams', 'DataPackageSearchService',
          'Principal', 'SimpleMessageToastService', 'LocationSimplifier',
          '$state',
          function($q, $stateParams,
              DataPackageSearchService, Principal, SimpleMessageToastService,
              LocationSimplifier, $state) {
            return LocationSimplifier.removeDollarSign($state, $stateParams,
              stateName).then(function() {
                var excludedAttributes = ['nested*','variables','questions',
                'instruments', 'dataSets', 'relatedPublications',
                'concepts'];
                var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                if (Principal.loginName() && !$stateParams.version) {
                  return DataPackageSearchService.findOneById(id, null,
                    excludedAttributes);
                } else {
                  var deferred = $q.defer();
                  loadShadowCopy(DataPackageSearchService,
                    SimpleMessageToastService, id,
                    $stateParams.version, excludedAttributes)
                    .then(deferred.resolve, deferred.reject);
                  return deferred;
                }
              }).catch($q.defer);
          }
        ]
      }
    };

    $stateProvider
      .state(stateName, dataPackageDetailConfig);

    var legacyStudyDetailConfig = angular.copy(dataPackageDetailConfig);
    legacyStudyDetailConfig.url = '/studies/{id}?{access-way}' +
      '{derived-variables-identifier}' +
      '{page}{query}{repeated-measurement-identifier}{size}{type}{version}';

    $stateProvider
      .state('legacyStudyDetail', legacyStudyDetailConfig);

    $stateProvider
      .state('dataPackageEdit', {
        parent: 'site',
        url: '/data-packages/{id}/edit',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datapackagemanagement/views/' +
              'data-package-edit-or-create.html.tmpl',
            controller: 'DataPackageEditOrCreateController',
            controllerAs: 'ctrl'
          }
        },
        onEnter: ["$rootScope", "$timeout", function($rootScope, $timeout) {
          $timeout(function() {
            $rootScope.$broadcast('domain-object-editing-started');
          }, 500);
        }],
        onExit: ["$rootScope", "$timeout", function($rootScope, $timeout) {
          $timeout(function() {
            $rootScope.$broadcast('domain-object-editing-stopped');
          }, 500);
        }],
        resolve: {
          entity: ['$stateParams', 'DataPackageResource',
            function($stateParams, DataPackageResource) {
              return DataPackageResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });

    $stateProvider
      .state('dataPackageCreate', {
        parent: 'site',
        url: '/data-packages/new',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datapackagemanagement/views/' +
              'data-package-edit-or-create.html.tmpl',
            controller: 'DataPackageEditOrCreateController',
            controllerAs: 'ctrl'
          }
        },
        onEnter: ["$rootScope", "$timeout", function($rootScope, $timeout) {
          $rootScope.$broadcast('start-ignoring-404');
          $timeout(function() {
            $rootScope.$broadcast('domain-object-editing-started');
          }, 500);
        }],
        onExit: ["$rootScope", "$timeout", function($rootScope, $timeout) {
          $rootScope.$broadcast('stop-ignoring-404');
          $timeout(function() {
            $rootScope.$broadcast('domain-object-editing-stopped');
          }, 500);
        }],
        resolve: {
          entity: function() {
            return null;
          }
        }
      });
  }]);
