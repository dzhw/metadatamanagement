/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('DataSetUploadService',
  function(ExcelReaderService, DataSetBuilderService,
    DataSetDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'dataSet-management.log-messages.data-set.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'dataSet-management.log-messages.data-set.missing-id', {
              index: index + 1
            });
          uploadCount++;
          return upload();
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success();
            uploadCount++;
            return upload();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'dataSet');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadDataSets = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('dataSet');
      ExcelReaderService.readFileAsync(file).then(function(data) {
        objects = DataSetBuilderService.getDataSets(data,
          dataAcquisitionProjectId);
        DataSetDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          },
          upload,
          function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'dataSet');
            errorMessages.forEach(function(errorMessage) {
              JobLoggingService.error(errorMessage.message,
                errorMessage.translationParams);
            });
          });
      }, function(error) {
        console.log(error);
        JobLoggingService.cancel(
          'global.log-messages.unsupported-excel-file', {});
      });
    };
    return {
      uploadDataSets: uploadDataSets
    };
  });
