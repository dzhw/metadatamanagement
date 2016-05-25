/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('DataSetUploadService',
function(ExcelReaderService, DataSetBuilderService,
  DataSetDeleteResource, $translate, JobLoggingService,
  ErrorMessageResolverService) {
  var objects;
  var uploadCount;
  var upload = function() {
    if (uploadCount === objects.length) {
      JobLoggingService.finish($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.dataSet.uploadTerminated', {}));
    } else {
      if (!objects[uploadCount].id || objects[uploadCount].id === '') {
        var index = uploadCount;
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.dataSet.' +
            'missingId', {
            index: index + 1
          }));
        uploadCount++;
        return upload();
      } else {
        objects[uploadCount].$save().then(function() {
        JobLoggingService.success();
        uploadCount++;
        return upload();
      }).catch(function(error) {
        var errorMessage = ErrorMessageResolverService
        .getErrorMessage(error, 'dataSet');
        JobLoggingService.error(errorMessage);
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
      objects  = DataSetBuilderService.getDataSets(data,
          dataAcquisitionProjectId);
      DataSetDeleteResource.deleteByDataAcquisitionProjectId({
          dataAcquisitionProjectId: dataAcquisitionProjectId},
          upload, function(error) {
            var errorMessage = ErrorMessageResolverService
            .getErrorMessage(error, 'dataSet');
            JobLoggingService.error(errorMessage);
          });
    }, function(error) {
      console.log(error);
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.unsupportedTexFile', {}));
    });
  };
  return {
      uploadDataSets: uploadDataSets
    };
});
