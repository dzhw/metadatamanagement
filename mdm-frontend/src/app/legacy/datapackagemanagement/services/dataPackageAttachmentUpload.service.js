'use strict';
angular.module('metadatamanagementApp').service('DataPackageAttachmentUploadService', ['Upload', '$q', '$http', 
  function(Upload, $q, $http) {
    var uploadAttachment = function(attachment, metadata) {
        var deferred = $q.defer();
        if (!Upload.isFile(attachment) || attachment.size <= 0) {
          deferred.reject({invalidFile: true});
          return deferred.promise;
        }
        Upload.upload({
          url: '/api/data-packages/attachments',
          data: {
            dataPackageAttachmentMetadata: Upload.jsonBlob(metadata),
            file: attachment
          }
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };

    var deleteAllAttachments = function(dataPackageId) {
      return $http.delete('/api/data-packages/' +
        encodeURIComponent(dataPackageId) +
        '/attachments');
    };
    return {
      uploadAttachment: uploadAttachment,
      deleteAllAttachments: deleteAllAttachments
    };
  }]);

