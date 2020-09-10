'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    var loadShadowCopy = function(StudySearchService, SimpleMessageToastService,
                                  id, version, excludes) {
      var loadLatestShadowCopyFallback = function() {
        return StudySearchService.findShadowByIdAndVersion(id, null, excludes)
          .promise.then(function(result) {
            if (result) {
              return result;
            } else {
              SimpleMessageToastService.openAlertMessageToast(
                'study-management.detail.not-found', {id: id}, 5000);
              return null;
            }
          });
      };

      return StudySearchService.findShadowByIdAndVersion(id, version, excludes)
        .promise.then(function(result) {
          if (result) {
            return result;
          } else {
            return loadLatestShadowCopyFallback();
          }
        });
    };

    $urlRouterProvider.when('/de/studies/', '/de/error');
    $urlRouterProvider.when('/en/studies/', '/en/error');
    $stateProvider
      .state('studyDetail', {
        parent: 'site',
        url: '/studies/{id}?{version}{query}{page}{size}' +
          '{access-way}{type}{panel-identifier}{derived-variables-identifier}',
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
            templateUrl: 'scripts/studymanagement/views/' +
              'study-detail.html.tmpl',
            controller: 'StudyDetailController',
            controllerAs: 'ctrl'
          }
        },
        resolve: {
          entity: ['$q', '$stateParams', 'StudySearchService', 'Principal',
            'SimpleMessageToastService', 'LocationSimplifier',
            function($q, $stateParams,
                StudySearchService, Principal, SimpleMessageToastService,
                LocationSimplifier) {
              var excludedAttributes = ['nested*','variables','questions',
                'surveys','instruments', 'dataSets', 'relatedPublications',
                'concepts'];
              var id = LocationSimplifier.ensureDollarSign($stateParams.id);
              if (Principal.loginName() && !$stateParams.version) {
                return StudySearchService.findOneById(id, null,
                  excludedAttributes);
              } else {
                var deferred = $q.defer();
                loadShadowCopy(StudySearchService,
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
      .state('studyEdit', {
        parent: 'site',
        url: '/studies/{id}/edit',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/studymanagement/views/' +
              'study-edit-or-create.html.tmpl',
            controller: 'StudyEditOrCreateController',
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
          entity: ['$stateParams', 'StudyResource',
            function($stateParams, StudyResource) {
              return StudyResource.get({
                id: $stateParams.id
              });
            }
          ]
        }
      });

    $stateProvider
      .state('studyCreate', {
        parent: 'site',
        url: '/studies/new',
        data: {
          authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']
        },
        views: {
          'content@': {
            templateUrl: 'scripts/studymanagement/views/' +
              'study-edit-or-create.html.tmpl',
            controller: 'StudyEditOrCreateController',
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
