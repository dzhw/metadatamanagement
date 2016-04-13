/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
function(CustomModalService, ExcelReaderService, SurveyBuilderService,
  SurveyDeleteResource, $translate, JobLoggingService) {
  var objects;
  var upload = function() {
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
      CustomModalService.getModal($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'deleteMessages.deleteSurveys', {
            id: dataAcquisitionProjectId
          })).then(function(returnValue) {
            if (returnValue) {
              ExcelReaderService.readFileAsync(file).then(function(data) {
                objects  = SurveyBuilderService.getSurveys(data,
                  dataAcquisitionProjectId);
                SurveyDeleteResource.deleteByDataAcquisitionProjectId({
                    dataAcquisitionProjectId: dataAcquisitionProjectId},
                    upload, function(error) {
                      JobLoggingService.error(error);
                    });
              });
            }else {
              JobLoggingService.cancel($translate.instant(
                'metadatamanagementApp.dataAcquisitionProject.detail.' +
                'logMessages.survey.cancelled', {}));
            }
          });
    }else {
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.survey.cancelled', {}));
    }
  };
  return {
      uploadSurveys: uploadSurveys
    };
});
