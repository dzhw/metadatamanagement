/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('File', function($resource) {
    return $resource('api/files/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      }
    });
  });
