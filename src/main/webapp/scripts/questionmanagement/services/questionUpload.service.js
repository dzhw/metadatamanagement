/*jshint loopfunc: true */
/* global Blob */
'use strict';

angular.module('metadatamanagementApp').service('QuestionUploadService',
  function(ZipReaderService, QuestionResource, QuestionDeleteResource,
    JobLoggingService, ImageUploadService,
    ErrorMessageResolverService, $q, ElasticSearchAdminService) {
    var files;
    var objects;
    var successfullyUploadedQuestions;
    var uploadQuestionCount;
    var uploadImageCount;

    var uploadImages = function() {
      if (uploadImageCount === successfullyUploadedQuestions.length) {
        JobLoggingService.finish(
          'question-management.log-messages.question.upload-terminated', {
            total: JobLoggingService.getCurrentJob().total,
            errors: JobLoggingService.getCurrentJob().errors
          });
      } else {
        var path = 'images/' +
        successfullyUploadedQuestions[uploadImageCount].id + '.png';
        var image = new Blob([files[path]
          .asArrayBuffer()], {type: 'image/PNG'});
        ImageUploadService.uploadImage(image,
          successfullyUploadedQuestions[uploadImageCount].id).then(function() {
            uploadImageCount++;
            uploadImages();
          }, function(error) {
            var endErrorIndex = error.message.indexOf('----');
            var messageShort = error.message.substr(0, endErrorIndex).trim();
            JobLoggingService.error(messageShort);
            JobLoggingService.cancel(
              'data-set-management.log-messages.tex.cancelled', {});
            uploadImageCount++;
            uploadImages();
          });
      }
    };

    var uploadQuestion = function() {
      if (uploadQuestionCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          uploadImageCount = 0;
          uploadImages();
        });
      } else {
        if (!objects[uploadQuestionCount].id || objects[uploadQuestionCount]
          .id === '') {
          var index = uploadQuestionCount;
          JobLoggingService.error(
            'question-management.log-messages.question.missing-id', {
              index: index + 1
            });
          uploadQuestionCount++;
          return uploadQuestion();
        } else {
          objects[uploadQuestionCount].imageType = 'PNG';
          objects[uploadQuestionCount].$save().then(function() {
            JobLoggingService.success();
            successfullyUploadedQuestions.push(objects[uploadQuestionCount]);
            uploadQuestionCount++;
            return uploadQuestion();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'question');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadQuestionCount++;
            return uploadQuestion();
          });
        }
      }
    };

    var uploadQuestions = function(file, dataAcquisitionProjectId) {
      objects = [];
      successfullyUploadedQuestions = [];
      uploadQuestionCount = 0;
      JobLoggingService.start('question');
      ZipReaderService.readZipFileAsync(file)
        .then(function(zipFile) {
          try {
            var hasImagesFolder = zipFile.files['images/'].dir;
            if (hasImagesFolder) {
              files = zipFile.files;
              for (var key in files) {
                if (key.indexOf('.json') !== -1) {
                  var question = JSON.parse(files[key].asText());
                  if (zipFile.file('images/' + question.id + '.png')) {
                    question.dataAcquisitionProjectId =
                    dataAcquisitionProjectId;
                    objects.push(new QuestionResource(question));
                  }else {
                    JobLoggingService
                    .error('data-acquisition-project-management.' +
                      'log-messages.not-found-image-file', {
                        id: question.id
                      });
                  }
                }
              }
              QuestionDeleteResource.deleteByDataAcquisitionProjectId({
                  dataAcquisitionProjectId: dataAcquisitionProjectId
                },
                uploadQuestion,
                function(error) {
                  var errorMessages = ErrorMessageResolverService
                    .getErrorMessages(error, 'question');
                  errorMessages.subMessages.forEach(function(errorMessage) {
                    JobLoggingService.error(errorMessage.message,
                      errorMessage.translationParams);
                  });
                });
            } else {
              return $q.reject('unsupportedDirectoryStructure');
            }
          } catch (e) {
            return $q.reject('unsupportedDirectoryStructure');
          }
        }, function() {
          JobLoggingService.cancel(
            'global.log-messages.unsupported-zip-file', {});
        });
    };

    return {
      uploadQuestions: uploadQuestions
    };
  });
