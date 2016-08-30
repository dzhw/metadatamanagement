/* Author: Daniel Katzberg */
'use strict';

/* Study Resource */
angular.module('metadatamanagementApp')
  .factory('StudyResource', function($resource, $state) {
    return $resource('/api/studies/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        },
        interceptor: {
          responseError: function(response) {
            if (response.status === 404) {
              $state.go('error');
            }
          }
        },
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });
