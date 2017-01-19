/*jshint loopfunc: true */
/* global Blob */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('QuestionUploadService',
  function(FileReaderService, QuestionResource, QuestionDeleteResource,
    JobLoggingService, QuestionImageUploadService, CleanJSObjectService,
    ErrorMessageResolverService, $q, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog) {
    var questions = [];
    var images = {};
    var uploadQuestionCount;

    var uploadNextQuestion = function() {
      if (uploadQuestionCount === questions.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          var job = JobLoggingService.getCurrentJob();
          JobLoggingService.finish(
            'question-management.log-messages.question.upload-terminated', {
              totalQuestions: job.getCounts('question').total,
              totalImages: job.getCounts('image').total,
              totalErrors: job.errors
            }
          );
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (!questions[uploadQuestionCount].id || questions[
            uploadQuestionCount]
          .id === '') {
          var index = uploadQuestionCount;
          JobLoggingService.error({
            message: 'question-management.log-messages.question.missing-id',
            messageParams: {
              index: index + 1
            },
            objectType: 'question'
          });
        } else {
          questions[uploadQuestionCount].$save()
            .then(function() {
              JobLoggingService.success({
                objectType: 'question'
              });
              var imageFile = images[questions[uploadQuestionCount].number];
              return FileReaderService.readAsArrayBuffer(imageFile);
            }, function(error) {
              //unable to save question object
              var errorMessages = ErrorMessageResolverService
                .getErrorMessage(error, 'question');
              JobLoggingService.error({
                message: errorMessages.message,
                messageParams: errorMessages.translationParams,
                subMessages: errorMessages.subMessages,
                objectType: 'question'
              });
              return $q.reject('previouslyHandledError');
            })
            .then(function(imageFile) {
              var image = new Blob([imageFile], {
                type: 'image/png'
              });
              return QuestionImageUploadService.uploadImage(image,
                questions[uploadQuestionCount].id);
            }, function(error) {
              if (error !== 'previouslyHandledError') {
                //image file read error
                JobLoggingService.error({
                  message: 'question-management.log-messages.' +
                    'question.unable-to-read-image-file',
                  messageParams: {
                    file: images[questions[uploadQuestionCount].number]
                      .name
                  },
                  objectType: 'image'
                });
              }
              return $q.reject('previouslyHandledError');
            })
            .then(function() {
              JobLoggingService.success({
                objectType: 'image'
              });
            }, function(error) {
              if (error !== 'previouslyHandledError') {
                //image file upload error
                JobLoggingService.error({
                  message: 'question-management.log-messages.' +
                    'question.unable-to-upload-image-file',
                  messageParams: {
                    file: images[questions[uploadQuestionCount].number]
                      .name
                  },
                  objectType: 'image'
                });
              }
              return $q.reject('previouslyHandledError');
            })
            .finally(function() {
              uploadQuestionCount++;
              uploadNextQuestion();
            });
        }
      }
    };

    var uploadQuestions = function(files, dataAcquisitionProjectId) {
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        var confirm = $mdDialog.confirm()
          .title($translate.instant(
            'search-management.delete-messages.' +
            'delete-questions-title'))
          .textContent($translate.instant(
            'search-management.delete-messages.delete-questions', {
              id: dataAcquisitionProjectId
            }))
          .ariaLabel($translate.instant(
            'search-management.delete-messages.delete-questions', {
              id: dataAcquisitionProjectId
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));
        $mdDialog.show(confirm).then(function() {
          images = {};
          questions = [];
          uploadQuestionCount = 0;
          JobLoggingService.start('question');
          var questionFileReaders = [];
          files.forEach(function(file) {
            if (_.endsWith(file.name, '.json')) {
              var pathAsArray;
              var instrumentName;
              var instrumentNumber;
              if (file.path) {
                pathAsArray = _.split(file.path, '/');
              } else {
                pathAsArray = _.split(file.webkitRelativePath, '/');
              }
              instrumentName = pathAsArray[pathAsArray.length - 2];
              instrumentNumber = _.split(instrumentName, 'ins')[1];
              var questionNumber = _.split(file.name, '.json')[0];
              questionFileReaders.push(FileReaderService.readAsText(
                  file)
                .then(function(result) {
                  try {
                    var question = CleanJSObjectService.removeEmptyJsonObjects(
                      JSON.parse(result));
                    question.dataAcquisitionProjectId =
                      dataAcquisitionProjectId;
                    question.instrumentId =
                      dataAcquisitionProjectId +
                      '-ins' + instrumentNumber;
                    question.instrumentNumber =
                      instrumentNumber;
                    question.id = question.instrumentId + '-' +
                      questionNumber;
                    question.number = questionNumber;
                    var successors = [];
                    if (!CleanJSObjectService
                      .isNullOrEmpty(question.successors)) {
                      question.successors.forEach(function(successorNumber) {
                        successors.push(question.instrumentId + '-' +
                        successorNumber);
                      });
                    }
                    question.successors = successors;
                    question.imageType = 'PNG';
                    if (!images[question.number]) {
                      JobLoggingService.error({
                        message: 'question-management.' +
                          'log-messages.question.not-found-image-file',
                        messageParams: {
                          id: question.number
                        },
                        objectType: 'question'
                      });
                    } else {
                      questions.push(new QuestionResource(
                        question));
                    }
                  } catch (e) {
                    JobLoggingService.error({
                      message: 'global.log-messages.unable-to-parse-json-file',
                      messageParams: {
                        file: file.name
                      },
                      objectType: 'question'
                    });
                  }
                }, function() {
                  JobLoggingService.error({
                    message: 'global.log-messages.unable-to-read-file',
                    messageParams: {
                      file: file.name
                    },
                    objectType: 'question'
                  });
                }));
            }
            if (file.name.endsWith('.png')) {
              var questionNumberForImage = file.name.substring(0,
                file.name.indexOf('.png'));
              images[questionNumberForImage] = file;
            }
          });
          //after reading all jsons (in parallel) the questions are deleted
          $q.all(questionFileReaders).then(function() {
            return QuestionDeleteResource.deleteByDataAcquisitionProjectId({
              dataAcquisitionProjectId: dataAcquisitionProjectId
            }).$promise;
          }).then(uploadNextQuestion, function(error) {
            //delete failed
            JobLoggingService.cancel(
              'question-management.log-messages.question.' +
              'unable-to-delete', {});
            return $q.reject(error);
          });

        });
      }
    };

    return {
      uploadQuestions: uploadQuestions
    };
  });
