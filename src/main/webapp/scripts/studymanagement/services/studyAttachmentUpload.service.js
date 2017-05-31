'use strict';
angular.module('metadatamanagementApp').service(
  'StudyAttachmentUploadService',
  function(Upload, $q, $http) {
    var uploadAttachment = function(attachment, metadata) {
        var deferred = $q.defer();
        Upload.upload({
          url: '/api/studies/attachments',
          data: {
            studyAttachmentMetadata: Upload.jsonBlob(metadata),
            file: attachment
          }
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };

    var deleteAllAttachments = function(studyId) {
      return $http.delete('/api/studies/' + encodeURIComponent(studyId) +
      '/attachments');
    };
    return {
      uploadAttachment: uploadAttachment,
      deleteAllAttachments: deleteAllAttachments
    };
  });
