/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').service('TexUploadService',
function(Upload, FileResource, JobLoggingService) {
  var uploadTexTemplate = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      Upload.upload({
          url: 'api/data-sets/report',
          fields: {
            'id': dataAcquisitionProjectId
          },
          file: file
            //Upload and document could filled with data successfully
        }).success(function(gridFsFileName) {
          //Download automaticly data filled tex template
          FileResource.download(gridFsFileName).then(function(response) {
            saveAs(response.data.blob, file.name);
            JobLoggingService.success('SuccessMsg:....');
            JobLoggingService.finish();
          }).catch(function(error) {
            JobLoggingService.error(error);
            JobLoggingService.finish();
          });
          //Server hat issues with the tex file, send error to error output
        }).error(function(error) {
          var endErrorIndex = error.message.indexOf('----');
          var messageShort = error.message.substr(0, endErrorIndex).trim();
          JobLoggingService.error(messageShort);
          JobLoggingService.finish();
        });
    }
  };
  return {
      uploadTexTemplate: uploadTexTemplate
    };
});
