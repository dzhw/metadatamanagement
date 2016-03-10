/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('FileStorageCollection', function($resource) {
    return $resource('/api/files', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
