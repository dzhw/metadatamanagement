/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('File', function($resource) {
    return $resource('api/files/:fileName', {
      fileName: '@fileName',
      responseType: 'arraybuffer'
    }, {
      'get': {
        method: 'GET',
      }
    });
  });
