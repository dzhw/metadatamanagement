/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
  function(ExcelReaderService, SurveyBuilderService,
    SurveyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'survey-management.log-messages.survey.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'survey-management.log-messages.survey.missing-id', {
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
              .getErrorMessages(error, 'survey');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadSurveys = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('survey');
      ExcelReaderService.readFileAsync(file).then(function(data) {
        objects = SurveyBuilderService.getSurveys(data,
          dataAcquisitionProjectId);
        SurveyDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          },
          upload,
          function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'survey');
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
      uploadSurveys: uploadSurveys
    };
  });
