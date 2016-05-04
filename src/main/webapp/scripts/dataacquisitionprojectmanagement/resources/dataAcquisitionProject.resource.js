'use strict';

angular.module('metadatamanagementApp')
    .factory('DataAcquisitionProject', function($resource, $state) {
      return $resource('/api/data-acquisition-projects/:id',
        {id: '@id'}, {
        'get': {
          method: 'GET',
          params:  {projection: 'complete'},
          interceptor: {
            responseError: function() {
              $state.go('error');
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
