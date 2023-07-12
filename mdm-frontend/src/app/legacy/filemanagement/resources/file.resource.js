/* global Blob */
/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .service('FileResource', function($http) {
    this.download = function(fileName) {
      var url = '/public/files';
      if (fileName.lastIndexOf('/', 0) === 0) {
        url = url + fileName;
      } else {
        url = url + '/' + fileName;
      }

      return $http({
        method: 'GET',
        url: url,
        transformResponse: function(data, headers) {
          var fileBlob;
          var contentType = headers('content-type');
          if (data) {
            fileBlob = new Blob([data], {
              type: contentType
            });
          }
          return {
            blob: fileBlob
          };
        },
        responseType: 'arraybuffer'
      });
    };
  });
