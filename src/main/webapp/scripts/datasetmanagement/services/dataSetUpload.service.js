/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('DataSetUploadService',
function(ExcelReaderService, DataSetBuilderService,
  DataSetDeleteResource, $translate, JobLoggingService,
  ErrorMessageResolverService) {
  var objects;
  var upload = function() {
    var itemsToUpload = objects.length;
    var j = 0;
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.dataSet.' +
            'missingId', {
              index: i + 1
            }));
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.dataSet.uploadTerminated', {}));
        }
      } else {
        objects[i].$save().then(function() {
          JobLoggingService.success();
          j++;
          if (j === itemsToUpload) {
            JobLoggingService.finish($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.dataSet.uploadTerminated', {}));
          }
        }).catch(function(error) {
          var errorMessage = ErrorMessageResolverService
          .getErrorMessage(error, 'dataSet');
          JobLoggingService.error(errorMessage);
          j++;
          if (j === itemsToUpload) {
            JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.dataSet.uploadTerminated', {}));
          }
        });
      }
    }
  };
  var uploadDataSets = function(file, dataAcquisitionProjectId) {
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
