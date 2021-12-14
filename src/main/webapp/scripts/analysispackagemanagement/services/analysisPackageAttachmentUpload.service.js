'use strict';
angular.module('metadatamanagementApp').service(
  'AnalysisPackageAttachmentUploadService',
  function(Upload, $q, $http) {
    var uploadAttachment = function(attachment, metadata) {
        var deferred = $q.defer();
        if (!Upload.isFile(attachment) || attachment.size <= 0) {
          deferred.reject({invalidFile: true});
          return deferred.promise;
        }
        Upload.upload({
          url: '/api/analysis-packages/attachments',
          data: {
            analysisPackageAttachmentMetadata: Upload.jsonBlob(metadata),
            file: attachment
          }
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };

    var deleteAllAttachments = function(analysisPackageId) {
      return $http.delete('/api/analysis-packages/' +
        encodeURIComponent(analysisPackageId) +
        '/attachments');
    };
    return {
      uploadAttachment: uploadAttachment,
      deleteAllAttachments: deleteAllAttachments
    };
  });
