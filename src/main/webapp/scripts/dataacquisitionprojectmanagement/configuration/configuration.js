'use strict';

angular
  .module('metadatamanagementApp')
  .config(
    function($stateProvider) {
      $stateProvider
        .state('project-cockpit', {
          parent: 'site',
          url: '/project-cockpit',
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
          onEnter: function($timeout) {
            $timeout(function() {
            });
          }
        });
    });
