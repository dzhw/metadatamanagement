'use strict';
angular.module('metadatamanagementApp').service('ConceptAttachmentUploadService', ['Upload', '$q', '$http', 
  function(Upload, $q, $http) {
    var uploadAttachment = function(attachment, metadata) {
        var deferred = $q.defer();
        if (!Upload.isFile(attachment) || attachment.size <= 0) {
          deferred.reject({invalidFile: true});
          return deferred.promise;
        }
        Upload.upload({
          url: '/api/concepts/attachments',
          data: {
            conceptAttachmentMetadata: Upload.jsonBlob(metadata),
            file: attachment
          }
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };

    var deleteAllAttachments = function(conceptId) {
      return $http.delete('/api/concepts/' + encodeURIComponent(conceptId) +
      '/attachments');
    };
    return {
      uploadAttachment: uploadAttachment,
      deleteAllAttachments: deleteAllAttachments
    };
  }]);

