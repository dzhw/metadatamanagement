'use strict';

angular
  .module('metadatamanagementApp')
  .config([
  '$stateProvider',

    function($stateProvider) {
      $stateProvider
        .state('project-overview', {
          parent: 'site',
          url: '/projects/overview?limit&page&sort',
          data: {
            authorities: ['ROLE_PUBLISHER', 'ROLE_ADMIN']
          },
          views: {
            'content@': {
              templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
                'project-overview.html.tmpl',
              controller: 'ProjectOverviewController',
              controllerAs: 'ctrl'
            }
          }
        })
        .state('project-cockpit', {
          parent: 'site',
          url: '/projects/:id?:tab',
          params: {
            id: {
              value: null,
              squash: true
            },
            tab: {
              value: 'status',
              squash: true
            }
          },
          reloadOnSearch: false,
          data: {
            authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER', 'ROLE_ADMIN']
          },
          views: {
            'content@': {
              templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
                'project-cockpit.html.tmpl',
              controller: 'ProjectCockpitController'
            }
          },
          resolve: {
            projectDeferred: ['$stateParams', '$q', 'DataAcquisitionProjectResource', 'CurrentProjectService',
              function($stateParams, $q,
                                      DataAcquisitionProjectResource,
                                      CurrentProjectService) {
              var deferred = $q.defer();
              if ($stateParams.id) {
                DataAcquisitionProjectResource.get({id: $stateParams.id})
                  .$promise.then(function(project) {
                  deferred.resolve(project);
                }).catch(function() {
                  CurrentProjectService.setCurrentProject(undefined);
                  deferred.reject();
                });
              } else {
                CurrentProjectService.setCurrentProject(undefined);
                deferred.reject();
              }
              return deferred;
            }]
          },
          onEnter: ["$timeout", function($timeout) {
            $timeout(function() {
            });
          }]
        });
    }]);
