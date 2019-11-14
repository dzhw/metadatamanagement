'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(SurveySearchService,
      SimpleMessageToastService, id, version) {
        var loadLatestShadowCopyFallback = function() {
          return SurveySearchService.findShadowByIdAndVersion(id, null).promise
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
        return SurveySearchService.findShadowByIdAndVersion(id, version).promise
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
    $stateProvider
      .state('surveyDetail', {
        parent: 'site',
        url: '/surveys/{id}?{version}{query}{page}{size}',
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
              'SimpleMessageToastService', '$q',
            function($stateParams, SurveySearchService, Principal,
              SimpleMessageToastService, $q) {
              if (Principal.loginName() && !$stateParams.version) {
                return SurveySearchService.findOneById($stateParams.id);
              } else {
                var deferred = $q.defer();
                loadShadowCopy(SurveySearchService,
                  SimpleMessageToastService, $stateParams.id,
                  $stateParams.version).then(deferred.resolve, deferred.reject);
                return deferred;
              }
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
