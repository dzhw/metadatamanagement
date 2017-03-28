/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('SurveyUploadService',
  function(JobLoggingService, ExcelReaderService, SurveyResource,
    SurveyBuilderService, $q, $rootScope, ErrorMessageResolverService,
    SurveyAttachmentUploadService, SurveyResponseRateImageUploadService,
    CleanJSObjectService, ElasticSearchAdminService, $translate, $mdDialog,
    SurveyRepositoryClient) {
    var surveys;
    var attachments;
    var readyToUploadSurveys;
    var filesMap;
    var previouslyUploadedSurveyNumbers;
    var existingSurveys = {}; // map surveyId -> presentInExcel true/false
    var createSurveysFileMap = function(files, dataAcquisitionProjectId) {
      filesMap = {'surveys': {
        'dataAcquisitionProjectId': dataAcquisitionProjectId,
        'excelFile': '',
        'images': {},
        'attachments': {}
      }};
      files.forEach(function(file) {
        var path;
        if (file.path) {
          path = _.split(file.path, '/');
        } else {
          if (file.webkitRelativePath) {
            path = _.split(file.webkitRelativePath, '/');
          } else {
            path = [file.name];
          }
        }
        var parentFolder = _.last(_.initial(path));
        switch (parentFolder) {
          case 'images':
            filesMap.surveys.images[_.split(file.name, '.')[0]] = file;
          break;
          case 'attachments':
            filesMap.surveys.attachments[file.name] = file;
          break;
          default:
            if (file.name === 'surveys.xlsx') {
              filesMap.surveys.excelFile = file;
            }
          break;
        }
      });
    };
    var createResourcesFromExcel = function(files) {
      surveys = [];
      attachments = [];
      return $q(function(resolve, reject) {
        ExcelReaderService.readFileAsync(files.surveys.excelFile, true).then(
          function(excelContent) {
            if (excelContent.surveys) {
              excelContent.surveys.forEach(function(surveyFromExcel, index) {
                    var survey;
                    if (CleanJSObjectService.
                     isNullOrEmpty(surveyFromExcel.number)) {
                      JobLoggingService.error({
                        message: 'survey-management.log-messages.survey.' +
                        'missing-number',
                        messageParams: {
                          index: index + 1
                        },
                        objectType: 'survey'
                      });
                    } else {
                      survey = SurveyBuilderService
                      .buildSurvey(surveyFromExcel, files.surveys.
                        dataAcquisitionProjectId);
                      surveys.push(survey);
                      if (existingSurveys[survey.id]) {
                        existingSurveys[survey.id].presentInExcel = true;
                      }
                    }
                  });
            } else {
              JobLoggingService.cancel(
                'global.log-messages.unable-to-read-excel-sheet', {
                  sheet: 'surveys'
                });
              reject();
            }
            if (excelContent.attachments) {
              excelContent.attachments.forEach(function(attachmentFromExcel,
                index) {
                          if (CleanJSObjectService
                            .isNullOrEmpty(attachmentFromExcel.surveyNumber)) {
                            JobLoggingService.error({
                              message: 'survey-management.log-messages' +
                              '.survey-attachment.missing-survey-number',
                              messageParams: {
                                index: index + 1
                              },
                              objectType: 'attachment'
                            });
                          }
                          if (CleanJSObjectService.
                            isNullOrEmpty(attachmentFromExcel.filename)) {
                            JobLoggingService.error({
                              message: 'survey-management.log-messages' +
                              '.survey-attachment.missing-filename',
                              messageParams: {
                                index: index + 1
                              },
                              objectType: 'attachment'
                            });
                          }
                          attachments.push(SurveyBuilderService.
                            buildSurveyAttachmentMetadata(attachmentFromExcel,
                              files.surveys.dataAcquisitionProjectId));
                        });
            } else {
              JobLoggingService.cancel(
                 'global.log-messages.unable-to-read-excel-sheet', {
                    sheet: 'attachment'
                  });
              reject();
            }
            resolve();
          });
      });
    };
    var createReadyToUploadSurveys = function() {
      readyToUploadSurveys = [];
      var notFoundAttachmentsMap = {};
      surveys.forEach(function(survey) {
        var surveyDetailsObject = {
          'survey': survey,
          'images': {
            'de': filesMap.surveys.images[survey.number + '_responserate_de'],
            'en': filesMap.surveys.images[survey.number + '_responserate_en']
          },
          'attachments': []
        };
        attachments.forEach(function(attachment) {
          if (attachment.fileName) {
            if (filesMap.surveys.attachments[attachment.fileName]) {
              if (attachment.surveyId === survey.id) {
                surveyDetailsObject.attachments.push({
                  'metadata': attachment,
                  'file': filesMap.surveys.attachments[attachment.fileName]
                });
              }
            } else {
              if (!notFoundAttachmentsMap[attachment.fileName]) {
                JobLoggingService.error({
                  message: 'survey-management.log-messages' +
                  '.survey-attachment.file-not-found',
                  messageParams: {
                    filename: attachment.fileName
                  },
                  objectType: 'attachment'
                });
                notFoundAttachmentsMap[attachment.fileName] = true;
              }
            }
          }
        });
        readyToUploadSurveys.push(surveyDetailsObject);
      });
    };

    var uploadSurveyDetailObject = function(surveyDetailObject, uploadCount) {
      return $q(function(resolve) {
        if (previouslyUploadedSurveyNumbers[surveyDetailObject.survey.number]) {
          JobLoggingService.error({
            message: 'survey-management.log-messages' +
              '.survey.duplicate-survey-number',
            messageParams: {
              index: uploadCount + 1,
              number: surveyDetailObject.survey.number
            },
            objectType: 'instrument'
          });
          resolve();
        } else {
          surveyDetailObject.survey.$save().then(function() {
                previouslyUploadedSurveyNumbers[surveyDetailObject.survey.
                  number] = true;
                JobLoggingService.success({
                    objectType: 'survey'
                  });
                var asyncFilesUpload = $q.when();
                if (surveyDetailObject.images.de) {
                  asyncFilesUpload = asyncFilesUpload.then(function() {
                    return SurveyResponseRateImageUploadService.
                    uploadImage(surveyDetailObject.images.de,
                    surveyDetailObject.survey.id);
                  }).then(function() {
                    JobLoggingService.success({
                      objectType: 'image'
                    });
                  }).catch(function() {
                    JobLoggingService.error({
                      message: 'survey-management.log-messages.' +
                      'survey.unable-to-upload-image-file',
                      messageParams: {
                        file: surveyDetailObject.survey.number +
                        '_responserate_de.png'
                      },
                      objectType: 'image'
                    });
                  });
                }
                if (surveyDetailObject.images.en) {
                  asyncFilesUpload = asyncFilesUpload.then(function() {
                    return SurveyResponseRateImageUploadService.
                    uploadImage(surveyDetailObject.images.en,
                      surveyDetailObject.survey.id);
                  }).then(function() {
                    JobLoggingService.success({
                      objectType: 'image'
                    });
                  }).catch(function() {
                    JobLoggingService.error({
                      message: 'survey-management.log-messages.' +
                      'survey.unable-to-upload-image-file',
                      messageParams: {
                        file: surveyDetailObject.survey.number +
                        '_responserate_en.png'
                      },
                      objectType: 'image'
                    });
                  });
                }
                surveyDetailObject.attachments.forEach(function(attachment) {
                    asyncFilesUpload = asyncFilesUpload.then(function() {
                      return SurveyAttachmentUploadService.
                      uploadAttachment(attachment.file, attachment.metadata);
                    }).then(function() {
                      JobLoggingService.success({
                        objectType: 'attachment'
                      });
                    }).catch(function(error) {
                      // attachment upload failed
                      var errorMessage =
                      ErrorMessageResolverService
                      .getErrorMessage(error, 'survey',
                      'survey-attachment', attachment.file.name);
                      JobLoggingService.error({
                        message: errorMessage.message,
                        messageParams: errorMessage.translationParams,
                        subMessages: errorMessage.subMessages,
                        objectType: 'attachment'
                      });
                    });

                  });
                asyncFilesUpload.finally(function() {
                  resolve();
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
                resolve();
              });
        }
      });
    };

    var deleteAllSurveysNotPresentInExcel = function() {
      var promiseChain = $q.when();
      _.each(existingSurveys, function(existingSurvey) {
        if (!existingSurvey.presentInExcel) {
          promiseChain = promiseChain.then(function() {
            return SurveyResource.delete({id: existingSurvey.id}).$promise
            .catch(
              function(error) {
                console.log('Error when deleting survey:', error);
              });
          });
        }
      });
      return promiseChain;
    };

    var uploadAllSurveyDetailObject = function(index) {
      var currentIndex = index;
      if (currentIndex === readyToUploadSurveys.length) {
        deleteAllSurveysNotPresentInExcel().finally(function() {
          ElasticSearchAdminService.processUpdateQueue('surveys').finally(
            function() {
              var job = JobLoggingService.getCurrentJob();
              JobLoggingService.finish(
                'survey-management.log-messages.survey.upload-terminated', {
                  totalSurveys: job.getCounts('survey').total,
                  totalImages: job.getCounts('image').total,
                  totalAttachments: job.getCounts('attachment').total,
                  totalErrors: JobLoggingService.getCurrentJob().errors
                });
              $rootScope.$broadcast('upload-completed');
            });
        });
        return;
      }
      uploadSurveyDetailObject(readyToUploadSurveys[index], index).
      then(function() {
          return uploadAllSurveyDetailObject(index + 1);
        });
    };
    var startJob = function(files, dataAcquisitionProjectId) {
      JobLoggingService.start('survey');
      createSurveysFileMap(files,
        dataAcquisitionProjectId);
      createResourcesFromExcel(filesMap).then(function() {
          createReadyToUploadSurveys();
          previouslyUploadedSurveyNumbers = {};
          uploadAllSurveyDetailObject(0);
        });
    };
    var uploadSurveys = function(files, dataAcquisitionProjectId) {
      existingSurveys = {};
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        SurveyRepositoryClient.findByDataAcquisitionProjectId(
          dataAcquisitionProjectId).then(function(result) {
            result.data.forEach(function(survey) {
              existingSurveys[survey.id] = survey;
            });
            if (result.data.length > 0) {
              var confirm = $mdDialog.confirm().title($translate.instant(
                  'search-management.delete-messages.delete-surveys-title'))
                .textContent($translate.instant(
                  'search-management.delete-messages.delete-surveys', {
                    id: dataAcquisitionProjectId
                  })).ariaLabel($translate.instant(
                  'search-management.delete-messages.delete-surveys', {
                    id: dataAcquisitionProjectId
                  }))
                .ok($translate.instant('global.buttons.ok'))
                .cancel($translate.instant('global.buttons.cancel'));
              $mdDialog.show(confirm).then(function() {
                  startJob(files, dataAcquisitionProjectId);
                });
            } else {
              startJob(files, dataAcquisitionProjectId);
            }
          });
      }
    };
    return {
      uploadSurveys: uploadSurveys
    };
  });
