/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('AtomicQuestionUploadService',
  function(ExcelReaderService, AtomicQuestionBuilderService,
    AtomicQuestionDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'dataAcquisitionProject-management.detail.' +
            'logMessages.atomicQuestion.uploadTerminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'dataAcquisitionProject-management.' +
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
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'atomicQuestion');
            JobLoggingService.error(errorMessages.message,
                errorMessages.translationParams, errorMessages.subMessages);
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
          'dataAcquisitionProject-management.detail.' +
          'logMessages.unsupportedExcelFile', {});
      });
    };
    return {
      uploadAtomicQuestions: uploadAtomicQuestions
    };
  });
