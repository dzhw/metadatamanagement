/* global saveAs */
'use strict';

angular.module('metadatamanagementApp').service('DataSetReportService',
  function(Upload, FileResource, JobLoggingService, ZipWriterService,
    $timeout, $http, $log) {
    var uploadTexTemplate = function(files, dataAcquisitionProjectId) {
      JobLoggingService.start('dataSetReport');
      ZipWriterService.createZipFileAsync(files, true).then(function(file) {
        if (file !== null) {
          file.name = file.name || 'report.zip';
          Upload.upload({
            url: 'api/data-sets/report',
            fields: {
              'id': dataAcquisitionProjectId
            },
            file: file
            // jshint -W098
          }).success(function(data, status, headers) {
              // jshint +W098
              var pollUri = headers('location');
              var tick = function() {
                $http.get(pollUri).then(function(task) {
                  if (task.data.state === 'RUNNING') {
                    //running
                    $timeout(tick, 5000);
                  } else if (task.data.state === 'DONE') {
                    FileResource.download(task.data.location)
                    .then(function(response) {
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
                  } else if (task.data.state === 'FAILURE') {
                    task.data.errorList.errors.forEach(function(error) {
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
              ).catch(function(error) {
                  $log.error('Error when polling task', error);
                });
              };
              tick();
            }).error(function(error) {
            $log.error('Template Upload failed', error);
            JobLoggingService.cancel(
              'data-set-management.log-messages.tex.cancelled', {}
            );
          });
        } else {
          JobLoggingService.cancel(
            'data-set-management.log-messages.tex.cancelled', {});
        }
      }).catch(function() {
        JobLoggingService.cancel(
          'data-set-management.log-messages.tex.cancelled', {});
      });
    };
    return {
      uploadTexTemplate: uploadTexTemplate
    };
  });
