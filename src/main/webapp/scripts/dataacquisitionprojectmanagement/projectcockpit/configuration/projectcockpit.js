'use strict';

angular
  .module('metadatamanagementApp')
  .config(
    function($stateProvider) {
      $stateProvider
        .state('projectcockpit', {
          parent: 'site',
          url: '/projectcockpit',
          reloadOnSearch: false,
          data: {
            authorities: ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER', 'ROLE_ADMIN']
          },
          views: {
            'content@': {
              templateUrl: 'scripts/projectcockpit/views/' +
                'projectcockpit.html.tmpl',
              controller: 'ProjectCockpitController'
            }
          },
          onEnter: function($timeout) {
            $timeout(function() {
            });
          }
        });
    });
