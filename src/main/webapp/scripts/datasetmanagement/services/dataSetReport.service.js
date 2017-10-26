/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').service('DataSetReportService',
  function(Upload, FileResource, JobLoggingService, ZipWriterService) {
    var uploadTexTemplate = function(files, dataAcquisitionProjectId) {

      ZipWriterService.createZipFileAsync(files, true).then(function(file) {
        if (file !== null) {
          file.name = file.name || 'report.zip';
          JobLoggingService.start('dataSetReport');
          Upload.upload({
            url: 'api/data-sets/report',
            fields: {
              'id': dataAcquisitionProjectId
            },
            file: file
          }).success(function(gridFsFileName) {
            //Upload and document could filled with data successfully
            //Download automaticly data filled tex template
            FileResource.download(gridFsFileName).then(function(
              response) {
              JobLoggingService.success({
                message: 'data-set-management.log-messages.' +
                  'tex.upload-terminated'
              });
              saveAs(response.data.blob, file.name);
              JobLoggingService.finish(
                'data-set-management.log-messages.tex.saved', {}
              );
            }).catch(function(error) {
              JobLoggingService.error({
                message: error
              });
              JobLoggingService.cancel(
                'data-set-management.log-messages.tex.cancelled', {}
              );
            });
            //Server hat issues with the tex file, send error to error output
          }).error(function(error) {
            error.errors.forEach(function(error) {
              var invalidValue = error.invalidValue;
              if (error.message.indexOf('----') > -1) {
                var endErrorIndex = error.message.indexOf(
                  '----');
                invalidValue = error.message.substr(0,
                    endErrorIndex)
                  .trim();
              }
              var messageParams = {
                invalidValue: invalidValue,
                entity: error.entity,
                property: error.property
              };
              JobLoggingService.error({
                message: error.message,
                messageParams: messageParams
              });
            });
            JobLoggingService.cancel(
              'data-set-management.log-messages.tex.cancelled', {}
            );
          });
        } else {
          JobLoggingService.cancel(
            'data-set-management.log-messages.tex.cancelled', {});
        }
      });
    };
    return {
      uploadTexTemplate: uploadTexTemplate
    };
  });
