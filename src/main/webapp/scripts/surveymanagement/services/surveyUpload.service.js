/*jshint loopfunc: true */
/* @Author: Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp').service('SurveyUploadService', function(
  ExcelReaderService, SurveyBuilderService, SurveyDeleteResource,
  JobLoggingService, SurveyResponseRateImageUploadService,
  ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
  $q, $translate, $mdDialog, CleanJSObjectService) {
  //Create variables
  var surveys; //Array of Surveys
  var images = {}; //An object with all images
  var uploadSurveyCount; //Counter for uploaded Surveys
  // a map surveyNumber -> true
  var previouslyUploadedSurveyNumbers;
  /* Upload Method. This methods uploads the survey and the images */
  var upload = function() {
    //Toast if all surveys are uploaded
    if (uploadSurveyCount === surveys.length) {
      ElasticSearchAdminService.processUpdateQueue().finally(
        function() {
          var job = JobLoggingService.getCurrentJob();
          JobLoggingService.finish(
            'survey-management.log-messages.survey.upload-terminated', {
              totalSurveys: job.getCounts('survey').total,
              totalImages: job.getCounts('image').total,
              totalErrors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
      //Upload Survey with images
    } else {
      //Check for not existing survey
      if (!surveys[uploadSurveyCount].number ||
        surveys[uploadSurveyCount].number === '') {
        var index = uploadSurveyCount;
        JobLoggingService.error({
          message: 'survey-management.log-messages.survey.missing-number',
          messageParams: {
            index: index + 1
          },
          objectType: 'survey'
        });
        uploadSurveyCount++;
        return upload(); //Start next iteration of uploading a survey
      } else if (previouslyUploadedSurveyNumbers[surveys[
          uploadSurveyCount].number]) {
        JobLoggingService.error({
          message: 'survey-management.log-messages.survey.' +
            'duplicate-survey-number',
          messageParams: {
            index: uploadSurveyCount + 1,
            number: surveys[uploadSurveyCount].number
          },
          objectType: 'survey'
        });
        uploadSurveyCount++;
        return upload(); //Start next iteration of uploading a survey
        //Survey is and will be uploaded
      } else {
        var survey = surveys[uploadSurveyCount];
        //Upload Survey
        survey.$save().then(function() {
          JobLoggingService.success({
            objectType: 'survey'
          });
          previouslyUploadedSurveyNumbers[survey.number] = true;
          //Asyn Chain waits for all async uploaded images
          var asyncChain = $q.when();

          //Get ImageNames of all images as an array
          var imageKeys = Object.keys(images);
          imageKeys.forEach(function(imageName) {
            asyncChain = asyncChain.then(function() {
              //check for valid name at import. no valid name, no import!
              var imageNameGerman = survey.number +
                '_responserate_de';
              var imageNameEnglish = survey.number +
                '_responserate_en';
              if (imageName !== imageNameGerman &&
                imageName !== imageNameEnglish) {
                return;
              }

              //Upload Images.
              SurveyResponseRateImageUploadService.uploadImage(
                  images[
                    imageName], survey.id)
                .then(function() {
                    JobLoggingService.success({
                      objectType: 'image'
                    });
                  },
                  function(error) {
                    if (error !== 'previouslyHandledError') {
                      //image file read error
                      JobLoggingService.error({
                        message: 'survey-management.log-messages.' +
                          'survey.unable-to-upload-image-file',
                        messageParams: {
                          file: images[imageName]
                        },
                        objectType: 'image'
                      });
                    }
                    return $q.reject(
                      'previouslyHandledError'
                    );
                  });
            });
          });
          //Everything went well. Start uploading next survey
          asyncChain.finally(function() {
            uploadSurveyCount++;
            return upload();
          });
        }).catch(function(error) {
          //unable to save survey object
          var errorMessages = ErrorMessageResolverService
            .getErrorMessage(error, 'survey');
          JobLoggingService.error({
            message: errorMessages.message,
            messageParams: errorMessages.translationParams,
            subMessages: errorMessages.subMessages,
            objectType: 'survey'
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
    if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
      var confirm = $mdDialog.confirm()
        .title($translate.instant(
          'search-management.delete-messages.delete-surveys-title'))
        .textContent($translate.instant(
          'search-management.delete-messages.delete-surveys', {
            id: dataAcquisitionProjectId
          }))
        .ariaLabel($translate.instant(
          'search-management.delete-messages.delete-surveys', {
            id: dataAcquisitionProjectId
          }))
        .ok($translate.instant('global.buttons.ok'))
        .cancel($translate.instant('global.buttons.cancel'));
      $mdDialog.show(confirm).then(function() {
        uploadSurveyCount = 0;
        surveys = [];
        images = {};
        previouslyUploadedSurveyNumbers = {};
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
                        .getSurveys(rawSurveys,
                          dataAcquisitionProjectId);
                      //Error Handling for non readable excel file
                    }, function() {
                      JobLoggingService.cancel(
                        'global.log-messages.unable-to-read-file', {
                          file: 'surveys.xlsx'
                        });
                    }).finally(function() {
                      upload(); //Start uploading of surveys and images
                    });
                }
                //Prepare svg images for uploading
                //No valid name check at this moment,
                //because there are no survey ids
                //Just a check for svg image
                if (file.name.endsWith('.svg')) {
                  var surveyResponseName = file.name
                    .substring(0, file.name.indexOf('.svg'));
                  images[surveyResponseName] = file;
                }
              });
            },
            //Error Handling for non deleteable surveys
            function() {
              JobLoggingService.cancel(
                'survey.log-messages.survey.unable-to-delete');
            });

      });
    }
  };
  //Global methods
  return {
    uploadSurveys: uploadSurveys
  };
});
