/*jshint loopfunc: true */
/* global Blob */
'use strict';

angular.module('metadatamanagementApp').service('QuestionUploadService',
  function(FileReaderService, QuestionResource, QuestionDeleteResource,
    JobLoggingService, QuestionImageUploadService, CleanJSObjectService,
    ErrorMessageResolverService, $q, ElasticSearchAdminService, $rootScope) {
    var questions = [];
    var images = {};
    var uploadQuestionCount;

    var uploadNextQuestion = function() {
      if (uploadQuestionCount === questions.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          var job = JobLoggingService.getCurrentJob();
          JobLoggingService.finish(
            'question-management.log-messages.question.upload-terminated',
            {
              totalQuestions: job.getCounts('question').total,
              totalImages: job.getCounts('image').total,
              totalErrors: job.errors
            }
          );
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (!questions[uploadQuestionCount].id || questions[uploadQuestionCount]
          .id === '') {
          var index = uploadQuestionCount;
          JobLoggingService.error({
            message: 'question-management.log-messages.question.missing-id',
            messageParams: {index: index + 1},
            objectType: 'question'
          });
        } else {
          questions[uploadQuestionCount].$save()
          .then(function() {
            JobLoggingService.success({objectType: 'question'});
            var imageFile = images[questions[uploadQuestionCount].id];
            return FileReaderService.readAsArrayBuffer(imageFile);
          }, function(error) {
            //unable to save question object
            var errorMessages = ErrorMessageResolverService
            .getErrorMessage(error, 'question');
            JobLoggingService.error({message: errorMessages.message,
              messageParams: errorMessages.translationParams,
              subMessages: errorMessages.subMessages,
              objectType: 'question'});
            return $q.reject('previouslyHandledError');})
          .then(function(imageFile) {
            var image = new Blob([imageFile], {type: 'image/png'});
            return QuestionImageUploadService.uploadImage(image,
              questions[uploadQuestionCount].id);
          }, function(error) {
            if (error !== 'previouslyHandledError') {
              //image file read error
              JobLoggingService.error({
                message: 'question-management.log-messages.' +
                'question.unable-to-read-image-file',
                messageParams:
                {file: images[questions[uploadQuestionCount].id].name},
                objectType: 'image'});
            }
            return $q.reject('previouslyHandledError');})
          .then(function() {
            JobLoggingService.success({objectType: 'image'});
          }, function(error) {
            if (error !== 'previouslyHandledError') {
              //image file upload error
              JobLoggingService.error({
                message: 'question-management.log-messages.' +
                'question.unable-to-upload-image-file',
                messageParams:
                {file: images[questions[uploadQuestionCount].id].name},
                objectType: 'image'});
            }
            return $q.reject('previouslyHandledError');})
          .finally(function() {
            uploadQuestionCount++;
            uploadNextQuestion();
          });
        }
      }
    };

    var uploadQuestions = function(files, dataAcquisitionProjectId) {
      images = {};
      questions = [];
      uploadQuestionCount = 0;
      JobLoggingService.start('question');
      var questionFileReaders = [];
      files.forEach(function(file) {
        if (file.name.endsWith('.json')) {
          questionFileReaders.push(FileReaderService.readAsText(file)
            .then(function(result) {
              try {
                var question = CleanJSObjectService.removeEmptyJsonObjects(
                  JSON.parse(result));
                question.dataAcquisitionProjectId = dataAcquisitionProjectId;
                question.imageType = 'PNG';
                if (!images[question.id]) {
                  JobLoggingService.error({
                    message: 'question-management.' +
                    'log-messages.question.not-found-image-file',
                    messageParams: {id: question.id},
                    objectType: 'image'
                  });
                } else {
                  questions.push(new QuestionResource(question));
                }
              } catch (e) {
                JobLoggingService.error({
                  message: 'global.log-messages.unable-to-parse-json-file',
                  messageParams: {file: file.name},
                  objectType: 'question'
                });
              }
            }, function() {
              JobLoggingService.error({
                message: 'global.log-messages.unable-to-read-file',
                messageParams: {file: file.name},
                objectType: 'question'
              });
            }));
        }
        if (file.name.endsWith('.png')) {
          var questionId = file.name.substring(0, file.name.indexOf('.png'));
          images[questionId] = file;
        }
      });
      //after reading all jsons (in parallel) the questions are deleted
      $q.all(questionFileReaders).then(function() {
        return QuestionDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise;
      }).then(uploadNextQuestion, function(error) {
        //delete failed
        JobLoggingService.cancel(
            'question-management.log-messages.question.unable-to-delete', {});
        return $q.reject(error);
      });
    };

    return {
      uploadQuestions: uploadQuestions
    };
  });
