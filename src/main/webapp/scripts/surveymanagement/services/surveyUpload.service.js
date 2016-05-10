/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
function(ExcelReaderService, SurveyBuilderService,
  SurveyDeleteResource, $translate, JobLoggingService,
  ErrorMessageResolverService) {
  var objects;
  var upload = function() {
    var itemsToUpload = objects.length;
    var j = 0;
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.survey.' +
            'missingId', {
              index: i + 1
            }));
        j++;
        if (j === itemsToUpload) {
          JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.survey.uploadTerminated', {}));
        }
      } else {
        objects[i].$save().then(function() {
          JobLoggingService.success();
          j++;
          if (j === itemsToUpload) {
            JobLoggingService.finish($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.survey.uploadTerminated', {}));
          }
        }).catch(function(error) {
          var errorMessage = ErrorMessageResolverService
          .getErrorMessage(error, 'survey');
          JobLoggingService.error(errorMessage);
          j++;
          if (j === itemsToUpload) {
            JobLoggingService.finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.survey.uploadTerminated', {}));
          }
        });
      }
    }
  };
  var uploadSurveys = function(file, dataAcquisitionProjectId) {
    JobLoggingService.start('survey');
    ExcelReaderService.readFileAsync(file).then(function(data) {
      objects  = SurveyBuilderService.getSurveys(data,
          dataAcquisitionProjectId);
      SurveyDeleteResource.deleteByDataAcquisitionProjectId({
          dataAcquisitionProjectId: dataAcquisitionProjectId},
          upload, function(error) {
            var errorMessage = ErrorMessageResolverService
            .getErrorMessage(error, 'survey');
            JobLoggingService.error(errorMessage);
          });
    }, function(error) {
      console.log(error);
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.unsupportedFile', {}));
    });
  };
  return {
      uploadSurveys: uploadSurveys
    };
});
