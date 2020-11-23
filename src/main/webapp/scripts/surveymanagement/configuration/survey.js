'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(SurveySearchService,
      SimpleMessageToastService, id, version, excludes) {
        var loadLatestShadowCopyFallback = function() {
          return SurveySearchService.findShadowByIdAndVersion(id, null,
            excludes).promise
        .then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'survey-management.detail.not-found', {id: id});
              return null;
            }
          });
        };
        return SurveySearchService.findShadowByIdAndVersion(id, version,
          excludes).promise
          .then(function(result) {
            if (result) {
              return result;
            } else {
              return loadLatestShadowCopyFallback();
            }
          });
      };

    $urlRouterProvider.when('/de/surveys/', '/de/error');
    $urlRouterProvider.when('/en/surveys/', '/en/error');
    var stateName = 'surveyDetail';
    $stateProvider
      .state(stateName, {
        parent: 'site',
        url: '/surveys/{id}?{page}{query}{size}{type}{version}',
        reloadOnSearch: false,
        data: {
          authorities: []
        },
        params: {
          'search-result-index': null
        },
        views: {
          'content@': {
            templateUrl: 'scripts/surveymanagement/views/' +
              'survey-detail.html.tmpl',
            controller: 'SurveyDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$stateParams', 'SurveySearchService', 'Principal',
              'SimpleMessageToastService', '$q', 'LocationSimplifier', '$state',
            function($stateParams, SurveySearchService, Principal,
              SimpleMessageToastService, $q, LocationSimplifier, $state) {
              return LocationSimplifier.removeDollarSign($state, $stateParams,
                stateName).then(function() {
                  var excludedAttributes = ['nested*','variables','questions',
                  'instruments', 'dataSets', 'relatedPublications','concepts'];
                  var id = LocationSimplifier.ensureDollarSign($stateParams.id);
                  if (Principal.loginName() && !$stateParams.version) {
                    return SurveySearchService.findOneById(id, null,
                      excludedAttributes);
                  } else {
                    var deferred = $q.defer();
                    loadShadowCopy(SurveySearchService,
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

    $stateProvider
      .state('surveyEdit', {
        parent: 'site',
        url: '/surveys/{id}/edit',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/surveymanagement/views/' +
              'survey-edit-or-create.html.tmpl',
            controller: 'SurveyEditOrCreateController',
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
          entity: ['$stateParams', 'SurveyResource',
            function($stateParams, SurveyResource) {
              return SurveyResource.get({
                id: $stateParams.id
              });
            }
          ]
        },
      });

    $stateProvider
      .state('surveyCreate', {
        parent: 'site',
        url: '/surveys/new',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/surveymanagement/views/' +
              'survey-edit-or-create.html.tmpl',
            controller: 'SurveyEditOrCreateController',
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
        },
      });
  });
