/*jshint loopfunc: true */
/* @Author: Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp').service('SurveyUploadService', function(
  ExcelReaderService, SurveyBuilderService, SurveyDeleteResource,
  JobLoggingService, SurveyImageUploadService,
  ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
  FileReaderService, $q) {
  //Create variables
  var surveys; //Array of Surveys
  var images = {}; //An object with all images
  var uploadSurveyCount; //Counter for uploaded Surveys
  /* Upload Method. This methods uploads the survey and the images */
  var upload = function() {
    //Toast if all surveys are uploaded
    if (uploadSurveyCount === surveys.length) {
      ElasticSearchAdminService.processUpdateQueue().finally(
        function() {
          JobLoggingService.finish(
            'survey-management.log-messages.survey.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
      //Upload Survey with images
    } else {
      //Check for not existing survey
      if (!surveys[uploadSurveyCount].id ||
        surveys[uploadSurveyCount].id === '') {
        var index = uploadSurveyCount;
        JobLoggingService.error({
          message: 'survey-management.log-messages.survey.missing-id',
          messageParams: {
            index: index + 1
          }
        });
        uploadSurveyCount++;
        return upload(); //Start next iteration of uploading a survey
        //Survey is and will be uploaded
      } else {
        var survey = surveys[uploadSurveyCount];
        //Upload Survey
        survey.$save().then(function() {
            //Get ImageNames of all images as an array
            var imageKeys = Object.keys(images);
            imageKeys.forEach(function(imageName) {
              if (imageName.indexOf(survey.id) === -1) {
                return;
              }
              SurveyImageUploadService.uploadImage(images[imageName], survey.id)
                .then(function(uploadedImage) {
                    JobLoggingService.success();
                    return uploadedImage;
                  },
                  function(error) {
                    if (error !== 'previouslyHandledError') {
                      //image file read error
                      JobLoggingService.error({
                        message: 'survey-management.log-messages.' +
                          'survey.unable-to-upload-image-file',
                        messageParams: {
                          file: images[imageName]
                        }
                      });
                    }
                    return $q.reject(
                      'previouslyHandledError'
                    );
                  });
            });
          })
          //Everything went well. Start uploading next survey
          .then(function() {
            JobLoggingService.success();
            uploadSurveyCount++;
            return upload();
          }).catch(function(error) {
            //unable to save survey object
            var errorMessages = ErrorMessageResolverService
              .getErrorMessage(error, 'survey');
            JobLoggingService.error({
              message: errorMessages.message,
              messageParams: errorMessages.translationParams,
              subMessages: errorMessages.subMessages
            });
            uploadSurveyCount++;
            return upload();
          });
      }
    }
  };
  /* This method can called by external scripts/classes.
      It prepates the Upload. Survey information will be read out of the
      excel file. */
  var uploadSurveys = function(files, dataAcquisitionProjectId) {
    uploadSurveyCount = 0;
    surveys = [];
    images = {};
    JobLoggingService.start('survey');
    //Delete all old Surveys by Project Id
    SurveyDeleteResource.deleteByDataAcquisitionProjectId({
      dataAcquisitionProjectId: dataAcquisitionProjectId
    }).$promise
    .then(
      //After deleting read the excel file for survey information
      function() {
        files.forEach(function(file) {
          if (file.name === 'surveys.xlsx') {
            ExcelReaderService.readFileAsync(file)
              //Save survey information in an array
              .then(function(rawSurveys) {
                surveys = SurveyBuilderService
                  .getSurveys(rawSurveys, dataAcquisitionProjectId);
                upload(); //Start uploading of surveys and depending images
                //Error Handling for non readable excel file
              }, function() {
                JobLoggingService.cancel(
                  'global.log-messages.unable-to-read-file', {
                    file: 'surveys.xlsx'
                  });
              });
          }
          //Prepare svg images for uploading
          if (file.name.endsWith('.svg')) {
            var surveyResponseName = file.name
              .substring(0, file.name.indexOf('.svg'));
            images[surveyResponseName] = file;
          }
        });
      },
      //Error Handling for non deleteable surveys
      function() {
        JobLoggingService.cancel('survey.log-messages.survey.unable-to-delete');
      });
  };
  //Global methods
  return {
    uploadSurveys: uploadSurveys
  };
});
