/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('AtomicQuestionUploadService',
function(ExcelReaderService, AtomicQuestionBuilderService,
  AtomicQuestionDeleteResource, $translate, JobLoggingService,
  ErrorMessageResolverService) {
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
        var errorMessage = ErrorMessageResolverService
          .getErrorMessage(error, 'atomicQuestion');
        JobLoggingService.error(errorMessage);
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
    JobLoggingService.start('atomicQuestion');
    ExcelReaderService.readFileAsync(file).then(function(data) {
      objects  = AtomicQuestionBuilderService.getAtomicQuestions(data,
          dataAcquisitionProjectId);
      AtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId},
            upload, function(error) {
              var errorMessage = ErrorMessageResolverService
              .getErrorMessage(error, 'atomicQuestion');
              JobLoggingService.error(errorMessage);
            });
    }, function(error) {
      console.log(error);
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.unsupportedExcelFile', {}));
    });
  };
  return {
      uploadAtomicQuestions: uploadAtomicQuestions
    };
});
