/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('FileStorage', function($resource) {
    return $resource('api/tmp/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
      },
      'save': {
        method: 'PUT'
      }
    });
  });
