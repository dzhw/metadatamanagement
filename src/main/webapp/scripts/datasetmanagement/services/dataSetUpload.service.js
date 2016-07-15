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
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.dataSet.uploadTerminated', {
              total: objects.length,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.dataSet.' +
            'missingId', {
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
            errorMessages.forEach(function(errorMessage) {
              JobLoggingService.error(errorMessage.message,
                errorMessage.translationParams);
            });
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
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'logMessages.unsupportedExcelFile', {});
      });
    };
    return {
      uploadDataSets: uploadDataSets
    };
  });
