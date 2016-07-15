/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('AtomicQuestionUploadService',
  function(ExcelReaderService, AtomicQuestionBuilderService,
    AtomicQuestionDeleteResource, $translate, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.atomicQuestion.uploadTerminated', {
              total: objects.length,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.atomicQuestion.' +
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
            console.log(objects[uploadCount]);
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'atomicQuestion');
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
    var uploadAtomicQuestions = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('atomicQuestion');
      ExcelReaderService.readFileAsync(file).then(function(data) {
        objects = AtomicQuestionBuilderService.getAtomicQuestions(data,
          dataAcquisitionProjectId);
        AtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          },
          upload,
          function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'atomicQuestion');
            errorMessages.forEach(function() {
              JobLoggingService.error(errorMessages.message,
                errorMessages.translationParams);
            });
          });
      }, function() {
        JobLoggingService.cancel(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'logMessages.unsupportedExcelFile', {});
      });
    };
    return {
      uploadAtomicQuestions: uploadAtomicQuestions
    };
  });
