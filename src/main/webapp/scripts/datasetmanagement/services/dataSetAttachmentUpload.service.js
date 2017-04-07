'use strict';
angular.module('metadatamanagementApp').service(
  'DataSetAttachmentUploadService',
  function(Upload, $q, $http) {
    var uploadAttachment = function(file, metadata) {
      var deferred = $q.defer();
      Upload.upload({
        url: '/api/data-sets/attachments',
        data: {
          dataSetAttachmentMetadata: Upload.jsonBlob(metadata),
          file: file
        }
      }).success(function() {
        deferred.resolve();
      }).error(function(error) {
        deferred.reject(error);
      });
      return deferred.promise;
    };

    var deleteAllAttachments = function(dataSetId) {
      return $http.delete('/api/data-sets/' + encodeURIComponent(dataSetId) +
        '/attachments');
    };
    return {
      uploadAttachment: uploadAttachment,
      deleteAllAttachments: deleteAllAttachments
    };
  });
