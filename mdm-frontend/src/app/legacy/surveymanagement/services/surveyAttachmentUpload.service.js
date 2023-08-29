'use strict';
angular.module('metadatamanagementApp').service(
  'SurveyAttachmentUploadService',
  function(Upload, $q, $http) {
    var uploadAttachment = function(file, metadata) {
        var deferred = $q.defer();
        if (!Upload.isFile(file) || file.size <= 0) {
          deferred.reject({invalidFile: true});
          return deferred.promise;
        }
        Upload.upload({
          url: '/api/surveys/attachments',
          data: {
            surveyAttachmentMetadata: Upload.jsonBlob(metadata),
            file: file
          }
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };

    var deleteAllAttachments = function(surveyId) {
      return $http.delete('/api/surveys/' + encodeURIComponent(surveyId) +
      '/attachments');
    };
    return {
      uploadAttachment: uploadAttachment,
      deleteAllAttachments: deleteAllAttachments
    };
  });
