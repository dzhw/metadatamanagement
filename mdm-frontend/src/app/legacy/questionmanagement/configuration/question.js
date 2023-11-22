'use strict';

angular.module('metadatamanagementApp')
  .config([
  '$stateProvider',
  '$urlRouterProvider',
function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(QuestionSearchService,
      SimpleMessageToastService, id, version, excludes) {
        var loadLatestShadowCopyFallback = function() {
          return QuestionSearchService.findShadowByIdAndVersion(id, null,
            excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'question-management.detail.not-found', {id: id});
              return null;
            }
          });
        };
        return QuestionSearchService.findShadowByIdAndVersion(id, version,
          excludes).promise.then(function(result) {
            if (result) {
              return result;
            } else {
              return loadLatestShadowCopyFallback();
            }
          });
      };
    var stateName = 'questionDetail';
    $urlRouterProvider.when('/de/questions/', '/de/error');
    $urlRouterProvider.when('/en/questions/', '/en/error');
    $stateProvider
      .state(stateName, {
        parent: 'site',
        url: '/questions/{id}?{page}{query}{size}{type}{version}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
        },
        views: {
          'content@': {
            templateUrl: 'scripts/questionmanagement/views/' +
              'question-detail.html.tmpl',
            controller: 'QuestionDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'QuestionSearchService', 'Principal',
          'SimpleMessageToastService', '$q', 'LocationSimplifier', '$state',
          function($stateParams, QuestionSearchService, Principal,
            SimpleMessageToastService, $q, LocationSimplifier, $state) {
            return LocationSimplifier.removeDollarSign($state, $stateParams,
              stateName).then(function() {
                var excludedAttributes = ['nested*', 'dataSets',
                'variables','relatedPublications','concepts'];
                var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                if (Principal.loginName() && !$stateParams.version) {
                  return QuestionSearchService.findOneById(id, null,
                    excludedAttributes);
                } else {
                  var deferred = $q.defer();
                  loadShadowCopy(QuestionSearchService,
                    SimpleMessageToastService, id,
                    $stateParams.version, excludedAttributes)
                    .then(deferred.resolve, deferred.reject);
                  return deferred;
                }
              }).catch($q.defer);
          }]
        }
      });
  }]);
