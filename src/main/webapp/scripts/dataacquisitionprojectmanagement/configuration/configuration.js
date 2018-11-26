'use strict';

angular
  .module('metadatamanagementApp')
  .config(
    function($stateProvider) {
      $stateProvider
        .state('project-cockpit', {
          parent: 'site',
          url: '/projects/:id?',
          params: {
            id: {
              value: null,
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
            projectDeferred: function($stateParams, $q,
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
            }
          },
          onEnter: function($timeout) {
            $timeout(function() {
            });
          }
        });
    });
