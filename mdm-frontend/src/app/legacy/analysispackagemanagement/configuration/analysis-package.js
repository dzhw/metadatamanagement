'use strict';

angular.module('metadatamanagementApp')
  .config([
  '$stateProvider',
  '$urlRouterProvider',
function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(AnalysisPackageSearchService,
        SimpleMessageToastService, id, version, excludes) {
      var loadLatestShadowCopyFallback = function() {
        return AnalysisPackageSearchService.findShadowByIdAndVersion(id, null,
          excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'analysis-package-management.detail.not-found', {id: id}, 5000);
              return null;
            }
          });
      };

      return AnalysisPackageSearchService.findShadowByIdAndVersion(id, version,
        excludes).promise.then(function(result) {
          if (result) {
            return result;
          } else {
            return loadLatestShadowCopyFallback();
          }
        });
    };

    $urlRouterProvider.when('/de/analysis-packages/', '/de/error');
    $urlRouterProvider.when('/en/analysis-packages/', '/en/error');
    var stateName = 'analysisPackageDetail';
    var analysisPackageDetailConfig = {
      parent: 'site',
      url: '/analysis-packages/{id}?{access-way}' +
        '{derived-variables-identifier}{page}{query}' +
        '{repeated-measurement-identifier}{size}{type}{version}',
      reloadOnSearch: false,
      data: {
        authorities: []
      },
      params: {
        'search-result-index': null
      },
      views: {
        'content@': {
          templateUrl: 'scripts/analysispackagemanagement/views/' +
            'analysis-package-detail.html.tmpl',
          controller: 'AnalysisPackageDetailController',
          controllerAs: 'ctrl'
        }
      },
      resolve: {
        entity: ['$q', '$stateParams', 'AnalysisPackageSearchService',
          'Principal', 'SimpleMessageToastService', 'LocationSimplifier',
          '$state',
          function($q, $stateParams, AnalysisPackageSearchService,
                   Principal, SimpleMessageToastService,
            LocationSimplifier, $state) {
            return LocationSimplifier.removeDollarSign($state, $stateParams,
              stateName).then(function() {
                var excludedAttributes = ['nested*', 'variables','questions',
                'instruments', 'dataSets', 'relatedPublications',
                'concepts'];
                var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                if (Principal.loginName() && !$stateParams.version) {
                  return AnalysisPackageSearchService.findOneById(id, null,
                    excludedAttributes);
                } else {
                  var deferred = $q.defer();
                  loadShadowCopy(AnalysisPackageSearchService,
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
      .state(stateName, analysisPackageDetailConfig);

    // var legacyStudyDetailConfig = angular.copy(analysisPackageDetailConfig);
    // legacyStudyDetailConfig.url = '/studies/{id}?{access-way}' +
    //   '{derived-variables-identifier}' +
    //   '{page}{query}{repeated-measurement-identifier}{size}{type}{version}';
    //
    // $stateProvider
    //   .state('legacyStudyDetail', legacyStudyDetailConfig);

    $stateProvider
      .state('analysisPackageEdit', {
        parent: 'site',
        url: '/analysis-packages/{id}/edit',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/analysispackagemanagement/views/' +
              'analysis-package-edit-or-create.html.tmpl',
            controller: 'AnalysisPackageEditOrCreateController',
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
          entity: ['$stateParams', 'AnalysisPackageResource',
            function($stateParams, AnalysisPackageResource) {
              return AnalysisPackageResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });

    $stateProvider
      .state('analysisPackageCreate', {
        parent: 'site',
        url: '/analysis-packages/new',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/analysispackagemanagement/views/' +
              'analysis-package-edit-or-create.html.tmpl',
            controller: 'AnalysisPackageEditOrCreateController',
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
