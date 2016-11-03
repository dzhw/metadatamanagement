/*jshint loopfunc: true */
/* global Blob */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
  function(ExcelReaderService, SurveyBuilderService,
    SurveyDeleteResource, JobLoggingService, SurveyImageUploadService,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
    FileReaderService, $q) {
    var surveys;
    var images = {};
    var uploadSurveyCount;

    var upload = function() {
      if (uploadSurveyCount === surveys.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'survey-management.log-messages.survey.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (!surveys[uploadSurveyCount].id ||
          surveys[uploadSurveyCount].id === '') {
          var index = uploadSurveyCount;
          JobLoggingService.error(
            'survey-management.log-messages.survey.missing-id', {
              index: index + 1
            });
          uploadSurveyCount++;
          return upload();
        } else {
          surveys[uploadSurveyCount].$save()
            .then(function() {
              var imageResponseRateNameDe =
                surveys[uploadSurveyCount].id + '_responserate_de';
              var responseRateDe = images[imageResponseRateNameDe];

              return FileReaderService.readAsArrayBuffer(responseRateDe);
            }, function(error) {
              //unable to save question object
              var errorMessages = ErrorMessageResolverService
                .getErrorMessages(error, 'question');
              JobLoggingService.error(errorMessages.message,
                errorMessages.translationParams, errorMessages.subMessages
              );
              return $q.reject('previouslyHandledError');
            })
            .then(function(responseRateDe) {
              var image = new Blob([responseRateDe], {
                type: 'image/svg+xml'
              });
              return SurveyImageUploadService.uploadImage(image,
                surveys[uploadSurveyCount].id);
            }, function(error) {
              if (error !== 'previouslyHandledError') {
                var imageResponseRateNameDe =
                  surveys[uploadSurveyCount].id + '_responserate_de';
                //image file read error
                JobLoggingService.error(
                  'question-management.log-messages.' +
                  'question.unable-to-read-image-file', {
                    file: images[imageResponseRateNameDe]
                  });
              }
              return $q.reject('previouslyHandledError');
            })
            .then(function() {
              JobLoggingService.success();
              uploadSurveyCount++;
              return upload();
            }).catch(function(error) {
              var errorMessages = ErrorMessageResolverService
                .getErrorMessages(error, 'survey');
              JobLoggingService.error(errorMessages.message,
                errorMessages.translationParams, errorMessages.subMessages
              );
              uploadSurveyCount++;
              return upload();
            });
        }
      }
    };

    var uploadSurveys = function(files, dataAcquisitionProjectId) {
      uploadSurveyCount = 0;
      surveys = [];
      images = {};
      JobLoggingService.start('survey');
      SurveyDeleteResource.deleteByDataAcquisitionProjectId({
        dataAcquisitionProjectId: dataAcquisitionProjectId
      }).$promise.then(
        function() {
          files.forEach(function(file) {
            if (file.name === 'surveys.xlsx') {
              ExcelReaderService.readFileAsync(file)
                .then(function(rawSurveys) {
                  surveys = SurveyBuilderService.getSurveys(
                    rawSurveys, dataAcquisitionProjectId);
                  upload();
                }, function() {
                  JobLoggingService.cancel('global.log-messages.' +
                    'unable-to-read-file', {
                      file: 'surveys.xlsx'
                    });
                });
            }
            if (file.name.endsWith('.svg')) {
              var surveyResponseName =
                file.name.substring(0, file.name.indexOf('.svg'));
              images[surveyResponseName] = file;
            }
          });
        },
        function() {
          JobLoggingService.cancel(
            'survey.log-messages.' +
            'survey.unable-to-delete');
        }
      );
    };
    return {
      uploadSurveys: uploadSurveys
    };
  });
