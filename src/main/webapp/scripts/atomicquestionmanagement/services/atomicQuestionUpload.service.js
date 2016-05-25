/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('AtomicQuestionUploadService',
function(ExcelReaderService, AtomicQuestionBuilderService,
  AtomicQuestionDeleteResource, $translate, JobLoggingService,
  ErrorMessageResolverService) {
  var objects;
  var uploadCount;
  var upload = function() {
    if (uploadCount === objects.length) {
      JobLoggingService.finish($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.atomicQuestion.uploadTerminated', {}));
    } else {
      if (!objects[uploadCount].id || objects[uploadCount].id === '') {
        var index = uploadCount;
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.atomicQuestion.' +
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
        console.log(objects[uploadCount]);
        var errorMessage = ErrorMessageResolverService
        .getErrorMessage(error, 'atomicQuestion');
        JobLoggingService.error(errorMessage);
        uploadCount++;
        return upload();
      });
      }
    }
  };
  var uploadAtomicQuestions = function(file, dataAcquisitionProjectId) {
    uploadCount = 0;
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
