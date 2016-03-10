/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('FileStorageCollection', function($resource) {
    return $resource('/api/tmp', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
