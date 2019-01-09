'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectResource', function($resource, $rootScope) {
    return $resource('/api/data-acquisition-projects/:id/:type', {
      id: '@id',
      type: '@type'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE',
        interceptor: {
          response: function(response) {
            if (response.status === 200) {
              $rootScope.$broadcast('project-deleted');
            }
          }
        }
      },
      'deleteAllMetadataByType': {
        method: 'DELETE'
      }
    });
  });
