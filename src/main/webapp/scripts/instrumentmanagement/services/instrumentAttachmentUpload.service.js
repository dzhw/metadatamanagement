'use strict';
angular.module('metadatamanagementApp').service(
  'InstrumentAttachmentUploadService',
  function(Upload, $q) {
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
    return {
      uploadAttachment: uploadAttachment
    };
  });
