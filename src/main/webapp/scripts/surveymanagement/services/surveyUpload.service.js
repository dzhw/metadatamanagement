/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
function(ExcelReaderService, SurveyBuilderService,
  SurveyDeleteResource, $translate, JobLoggingService) {
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
        JobLoggingService.error(error);
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
    ExcelReaderService.readFileAsync(file).then(function(data) {
      if (data instanceof Error) {
        console.log(data);
      } else {
        JobLoggingService.start('survey');
        objects  = SurveyBuilderService.getSurveys(data,
          dataAcquisitionProjectId);
        SurveyDeleteResource.deleteByDataAcquisitionProjectId({
          dataAcquisitionProjectId: dataAcquisitionProjectId},
          upload, function(error) {
            JobLoggingService.error(error);
          });
      }
    });
  };
  return {
      uploadSurveys: uploadSurveys
    };
});
