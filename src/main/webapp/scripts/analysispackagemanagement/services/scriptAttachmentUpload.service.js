'use strict';
angular.module('metadatamanagementApp').service(
  'ScriptAttachmentUploadService',
  function(Upload, $q, $http) {
    var uploadScriptAttachment = function(file, metadata) {
        var deferred = $q.defer();
        if (!Upload.isFile(file) || file.size <= 0) {
          deferred.reject({invalidFile: true});
          return deferred.promise;
        }
        Upload.upload({
          url: '/api/analysis-packages/' + metadata.analysisPackageId +
            '/scripts/attachments',
          data: {
            file: file,
            scriptAttachmentMetadata: Upload.jsonBlob(metadata)
          }
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };

    var deleteAttachment = function(
      analysisPackageId,
      scriptUuid,
      fileName
    ) {
      return $http.delete('/api/analysis-packages/' +
        encodeURIComponent(analysisPackageId) + '/scripts/' +
        encodeURIComponent(scriptUuid) +
        '/attachments' +
        encodeURIComponent(fileName));
    };
    return {
      uploadScriptAttachment: uploadScriptAttachment,
      deleteScriptAttachment: deleteAttachment
    };
  });
