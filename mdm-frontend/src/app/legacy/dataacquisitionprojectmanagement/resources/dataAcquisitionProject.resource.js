'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectResource', ['$resource', '$rootScope',  function($resource, $rootScope) {
    return $resource('/api/data-acquisition-projects/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT',
        interceptor: {
          response: function(response) {
            if (response.status === 200) {
              $rootScope.$broadcast('project-saved');
            }
          }
        }
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
      }
    });
  }]);

