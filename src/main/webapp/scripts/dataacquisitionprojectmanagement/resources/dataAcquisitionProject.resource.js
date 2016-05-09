'use strict';

angular.module('metadatamanagementApp')
    .factory('DataAcquisitionProjectResource', function($resource, $state) {
      return $resource('/api/data-acquisition-projects/:id',
        {id: '@id'}, {
        'get': {
          method: 'GET',
          params:  {projection: 'complete'},
          interceptor: {
            responseError: function(response) {
              if (response.status !== 401) {
                $state.go('error');
              }
            }
          }
        },
        'save': {
          method: 'PUT'
        },
        'delete': {
          method: 'DELETE'
        }
      });
    });
