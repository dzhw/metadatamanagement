/* global Blob */
/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('File', function($resource) {
    return $resource('variable_report/:fileName', {
      fileName: '@fileName'
    }, {
      'download': {
        method: 'GET',
        responseType: 'arraybuffer',
        transformResponse: function(data, headers) {
          var fileBlob;
          var contentType = headers('content-type');
          if (data) {
            fileBlob = new Blob([data], {
              type: contentType
            });
          }
          return {
            response: fileBlob
          };
        }
      }
    });
  });
