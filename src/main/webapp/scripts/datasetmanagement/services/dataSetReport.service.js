/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').service('DataSetReportService',
  function(Upload, FileResource, JobLoggingService, ZipWriterService,
    $timeout, $resource) {
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
          }).success(function(data, status, headers) {
            //Upload and document could filled with data successfully
            //Download automaticly data filled tex template
            if (data.state === 'RUNNING' && status === 202) {
              //TODO
              var pollUri = headers('location');
              var tick = function() {
                //poll headers('location')
                var Task = $resource(pollUri);
                Task.get({}).$promise.then(function(task, status) {
                  console.log('polled:', pollUri);
                  console.log('task:', task);
                  console.log('status:', status);
                  if (status === 202) {
                    //running
                    $timeout(tick, 1000);
                  } if (status === 303 && task.state === 'DONE') {
                    // on data.state='DONE'
                    //    FileResource.download(data.location.string)
                    FileResource.download(task.location.sting).then(function(
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
                  } if (status === 500) {
                    // on data.state='FAILURE'
                    //    data.errorList.forEach(--- handle errorDTO)
                    // Server hat issues with the tex file,
                    // send error to error output
                    task.errorList.forEach(function(error) {
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
                  }
                }
              );
              };
            }
          }).error(function(error) {
            console.log('repprt request failed', error);
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
