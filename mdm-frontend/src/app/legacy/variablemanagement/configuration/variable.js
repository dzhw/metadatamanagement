'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(VariableSearchService,
      SimpleMessageToastService, id, version, excludes) {
        var loadLatestShadowCopyFallback = function() {
          return VariableSearchService.findShadowByIdAndVersion(id, null,
            excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'variable-management.detail.not-found', {id: id});
              return null;
            }
          });
        };
        return VariableSearchService.findShadowByIdAndVersion(id, version,
          excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              return loadLatestShadowCopyFallback();
            }
          });
      };
    $urlRouterProvider.when('/de/variables/', '/de/error');
    $urlRouterProvider.when('/en/variables/', '/en/error');
    var stateName = 'variableDetail';
    $stateProvider
      .state(stateName, {
        parent: 'site',
        url: '/variables/{id}?{page}{query}{size}{type}{version}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
        },
        views: {
          'content@': {
            templateUrl: 'scripts/variablemanagement/views/' +
              'variable-detail.html.tmpl',
            controller: 'VariableDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'VariableSearchService', 'Principal',
            'SimpleMessageToastService', '$q', 'LocationSimplifier', '$state',
            function($stateParams, VariableSearchService, Principal,
              SimpleMessageToastService, $q, LocationSimplifier, $state) {
              return LocationSimplifier.removeDollarSign($state, $stateParams,
                stateName).then(function() {
                  var excludedAttributes = ['nested*','questions',
                  'instruments','relatedPublications','concepts'];
                  var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                  if (Principal.loginName() && !$stateParams.version) {
                    return VariableSearchService.findOneById(id, null,
                      excludedAttributes);
                  } else {
                    var deferred = $q.defer();
                    loadShadowCopy(VariableSearchService,
                      SimpleMessageToastService, id,
                      $stateParams.version, excludedAttributes)
                      .then(deferred.resolve, deferred.reject);
                    return deferred;
                  }
                }
              ).catch($q.defer);
            }
          ]
        }
      });
  });
