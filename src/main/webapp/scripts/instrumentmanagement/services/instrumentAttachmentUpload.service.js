'use strict';
angular.module('metadatamanagementApp').service(
  'InstrumentAttachmentUploadService',
  function(Upload, $q, $http) {
    var uploadAttachment = function(attachment, metadata) {
        var deferred = $q.defer();
        Upload.upload({
          url: '/api/instruments/attachments',
          data: {
            instrumentAttachmentMetadata: Upload.jsonBlob(metadata),
            file: attachment
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
      deleteAllAttachments: deleteAllAttachments,
      uploadAttachment: uploadAttachment
    };
  });
