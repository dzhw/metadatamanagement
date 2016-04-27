/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('AtomicQuestionUploadService',
function(CustomModalService, ExcelReaderService, AtomicQuestionBuilderService,
  AtomicQuestionDeleteResource, $translate, JobLoggingService) {
  var objects;
  var upload = function() {
    var itemsToUpload = objects.length;
    var j = 0;
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.atomicQuestion.' +
            'missingId', {
              index: i + 1
            }));
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.atomicQuestion.uploadTerminated', {}));
        }
      } else {
        objects[i].$save().then(function() {
          JobLoggingService.success();
          j++;
          if (j === itemsToUpload) {
            JobLoggingService.finish($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.atomicQuestion.uploadTerminated', {}));
          }
        }).catch(function(error) {
        JobLoggingService.error(error);
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.atomicQuestion.uploadTerminated', {}));
        }
      });
      }
    }
  };
  var uploadAtomicQuestions = function(file, dataAcquisitionProjectId) {
    if (file !== null) {
      CustomModalService.getModal($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'deleteMessages.deleteAtomicQuestions', {
            id: dataAcquisitionProjectId
          })).then(function(returnValue) {
            if (returnValue) {
              ExcelReaderService.readFileAsync(file).then(function(data) {
                objects  = AtomicQuestionBuilderService.getAtomicQuestions(data,
                  dataAcquisitionProjectId);
                AtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId({
                    dataAcquisitionProjectId: dataAcquisitionProjectId},
                    upload, function(error) {
                      JobLoggingService.error(error);
                    });
              });
            }else {
              JobLoggingService.cancel($translate.instant(
                'metadatamanagementApp.dataAcquisitionProject.detail.' +
                'logMessages.atomicQuestion.cancelled', {}));
            }
          });
    }else {
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.atomicQuestion.cancelled', {}));
    }
  };
  return {
      uploadAtomicQuestions: uploadAtomicQuestions
    };
});
