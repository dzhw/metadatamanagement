/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('StudyUploadService',
  function(ExcelReaderService, StudyBuilderService,
    StudyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'study-management.log-messages.study.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'study-management.log-messages.study.missing-id', {
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
              .getErrorMessages(error, 'study');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadStudies = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('study');
      ExcelReaderService.readFileAsync(file).then(function(data) {
        objects = StudyBuilderService.getStudies(data,
          dataAcquisitionProjectId);
        StudyDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          },
          upload,
          function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'study');
            errorMessages.forEach(function(errorMessage) {
              JobLoggingService.error(errorMessage.message,
                errorMessage.translationParams);
            });
          });
      }, function() {
        JobLoggingService.cancel(
          'global.log-messages.unsupported-excel-file', {});
      });
    };
    return {
      uploadStudies: uploadStudies
    };
  });
