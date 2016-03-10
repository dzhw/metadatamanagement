/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('FileCollection', function($resource) {
    return $resource('/api/files', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
