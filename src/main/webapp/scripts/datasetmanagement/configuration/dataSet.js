'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('/de/data-sets/', '/de/error');
    $urlRouterProvider.when('/en/data-sets/', '/en/error');
    $stateProvider
      .state('dataSetDetail', {
        parent: 'site',
        url: '/data-sets/{id}?{search-result-index}',
        reloadOnSearch: false,
        data: {
          authorities: []
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
          entity: ['$stateParams', 'DataSetSearchService',
            function($stateParams, DataSetSearchService) {
              return DataSetSearchService.findOneById($stateParams.id);
            }
          ]
        },
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
        },
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
        },
      });
  });
