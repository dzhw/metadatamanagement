'use strict';
angular.module('metadatamanagementApp').service(
  'InstrumentAttachmentUploadService',
  function(Upload, $q, $http) {
    var uploadAttachment = function(file, metadata) {
        var deferred = $q.defer();
        if (!Upload.isFile(file) || file.size <= 0) {
          deferred.reject({invalidFile: true});
          return deferred.promise;
        }
        Upload.upload({
          url: '/api/instruments/attachments',
          data: {
            instrumentAttachmentMetadata: Upload.jsonBlob(metadata),
            file: file
          }
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };

    var deleteAllAttachments = function(instrumentId) {
      return $http.delete('/api/instruments/' +
        encodeURIComponent(instrumentId) + '/attachments');
    };
    return {
      uploadAttachment: uploadAttachment,
      deleteAllAttachments: deleteAllAttachments
    };
  });