/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
function(CustomModal, ExcelReader, SurveyBuilder, SurveyDeleteResource,
  $translate, JobLoggingService) {
  var upload = function(objects) {
    var itemsToUpload = objects.length;
    var j = 0;
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.Survey.' +
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
        objects[i].$save().then(function(data) {
          JobLoggingService.success($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.survey.saved', {
              id: data.id
            }));
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
    if (file !== null) {
      CustomModal.getModal($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'deleteMessages.deleteSurveys', {
            id: dataAcquisitionProjectId
          })).then(function(returnValue) {
            if (returnValue) {
              ExcelReader.readFileAsync(file).then(function(data) {
                var objects  = SurveyBuilder.getSurveys(data,
                  dataAcquisitionProjectId);
                SurveyDeleteResource.deleteByDataAcquisitionProjectId({
                    dataAcquisitionProjectId: dataAcquisitionProjectId},
                    upload(objects), function() {
                      //JobLog.error('Unknown Error');
                    });
              });
            }else {
              JobLoggingService.cancel('canceld');
            }
          });
    }
  };
  return {
      uploadSurveys: uploadSurveys
    };
});
