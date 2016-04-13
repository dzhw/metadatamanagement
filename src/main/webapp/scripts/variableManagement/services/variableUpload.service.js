/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
function(CustomModalService, $translate, ZipReaderService,
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
        objects[i].$save().then(function(data) {
          JobLoggingService.success($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.saved', {
              id: data.id
            }));
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
    if (file !== null) {
      CustomModalService.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteVariables', {
              id: dataAcquisitionProjectId
            })).then(function(returnValue) {
              if (returnValue) {
                ZipReaderService.readZipFileAsync(file)
                   .then(function(files) {
                     objects = VariableBuilderService.getVariables(files,
                     dataAcquisitionProjectId);
                     VariableDeleteResource.deleteByDataAcquisitionProjectId({
                           dataAcquisitionProjectId: dataAcquisitionProjectId},
                          upload, function(error) {
                            JobLoggingService.error(error);
                          });
                   });
              }else {
                JobLoggingService.cancel($translate.instant(
                  'metadatamanagementApp.dataAcquisitionProject.detail.' +
                  'logMessages.variable.cancelled', {}));
              }
            });
    }else {
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.variable.cancelled', {}));
    }
  };
  return {
          uploadVariables: uploadVariables
        };
});
