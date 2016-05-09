/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
function($translate, ZipReaderService,
  VariableBuilderService, VariableDeleteResource, JobLoggingService) {
  var objects;
  var upload = function() {
    var itemsToUpload = objects.length;
    var j = 0;
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.variable.' +
            'missingId', {
              index: i + 1
            }));
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.uploadTerminated', {}));
        }
      } else {
        objects[i].$save().then(function() {
          JobLoggingService.success();
          j++;
          if (j === itemsToUpload) {
            JobLoggingService.finish($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.variable.uploadTerminated', {}));
          }
        }).catch(function(error) {
        JobLoggingService.error(error);
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.uploadTerminated', {}));
        }
      });
      }
    }
  };
  var uploadVariables = function(file, dataAcquisitionProjectId) {
    ZipReaderService.readZipFileAsync(file)
      .then(function(data) {
        if (data instanceof Error) {
          console.log(data);
          JobLoggingService.cancel($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.unsupportedFile', {}));
        } else {
          JobLoggingService.start('variable');
          objects = VariableBuilderService.getVariables(data,
          dataAcquisitionProjectId);
          VariableDeleteResource.deleteByDataAcquisitionProjectId({
          dataAcquisitionProjectId: dataAcquisitionProjectId},
          upload, function(error) {
            JobLoggingService.error(error);
          });
        }
      });
  };
  return {
          uploadVariables: uploadVariables
        };
});
