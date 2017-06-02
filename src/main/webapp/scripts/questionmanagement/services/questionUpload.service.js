/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('QuestionUploadService',
  function(FileReaderService, QuestionResource, QuestionRepositoryClient,
    JobLoggingService, QuestionImageUploadService, CleanJSObjectService,
    ErrorMessageResolverService, $q, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog, QuestionIdBuilderService, StudyIdBuilderService,
    InstrumentIdBuilderService) {
    var filesMap;
    var questionResources;
    // map questionId -> presentInJson true/false
    var existingQuestions = {};
    var createInstrumentsFileMap = function(files, dataAcquisitionProjectId) {
      filesMap = {};
      var instrumentIndex = 0;
      files.forEach(function(file) {
        var path;
        if (file.path) {
          path = _.split(file.path, '/');
        } else {
          if (file.webkitRelativePath) {
            path = _.split(file.webkitRelativePath, '/');
          }
        }
        var pathLength = path.length;
        var fileName = _.last(path);
        if (_.endsWith(fileName, '.json')) {
          if (!filesMap[path[pathLength - 2]]) {
            filesMap[path[pathLength - 2]] = {};
            filesMap[path[pathLength - 2]].dataAcquisitionProjectId =
              dataAcquisitionProjectId;
            filesMap[path[pathLength - 2]].instrumentName =
              path[pathLength - 2];
            filesMap[path[pathLength - 2]].instrumentNumber =
              _.split(path[pathLength - 2], 'ins')[1];
            filesMap[path[pathLength - 2]].instrumentIndex =
              instrumentIndex;
            filesMap[path[pathLength - 2]].jsonFiles = {};
            filesMap[path[pathLength - 2]].pngFiles = {};
            instrumentIndex++;
          }
          filesMap[path[pathLength - 2]]
            .jsonFiles[_.split(fileName, '.json')[0]] = file;
        }
        if (_.endsWith(fileName, '.png')) {
          if (!filesMap[path[pathLength - 3]]) {
            filesMap[path[pathLength - 3]] = {};
            filesMap[path[pathLength - 3]].dataAcquisitionProjectId =
              dataAcquisitionProjectId;
            filesMap[path[pathLength - 3]].instrumentName =
              path[pathLength - 3];
            filesMap[path[pathLength - 3]].instrumentNumber =
              _.split(path[pathLength - 3], 'ins')[1];
            filesMap[path[pathLength - 3]].instrumentIndex =
              instrumentIndex;
            filesMap[path[pathLength - 3]].pngFiles = {};
            filesMap[path[pathLength - 3]].jsonFiles = {};
            instrumentIndex++;
          }
          filesMap[path[pathLength - 3]]
            .pngFiles[_.split(fileName, '.png')[0]] = file;
        }
      });
    };
    var createQuestionResource = function(instrument,
      questionAsJson, questionNumber) {
      return $q(function(resolve) {
        FileReaderService.readAsText(questionAsJson)
          .then(function(result) {
            try {
              var question = CleanJSObjectService
                .removeEmptyJsonObjects(JSON.parse(result));
              question.dataAcquisitionProjectId = instrument
                .dataAcquisitionProjectId;
              question.instrumentId = InstrumentIdBuilderService
                .buildInstrumentId(instrument.dataAcquisitionProjectId,
                  instrument.instrumentNumber);
              question.instrumentNumber = instrument.instrumentNumber;
              question.id = QuestionIdBuilderService.buildQuestionId(
                question.dataAcquisitionProjectId,
                question.instrumentNumber,
                questionNumber);
              if (existingQuestions[question.id]) {
                existingQuestions[question.id].presentInJson = true;
              }
              question.number = questionNumber;
              var successors = [];
              if (!CleanJSObjectService.isNullOrEmpty(question
                  .successorNumbers)) {
                question.successorNumbers
                  .forEach(function(successorNumber) {
                    successors.push(QuestionIdBuilderService.buildQuestionId(
                      question.dataAcquisitionProjectId,
                      question.instrumentNumber,
                      successorNumber));
                  });
              }
              question.successors = successors;
              question.studyId = StudyIdBuilderService
                .buildStudyId(question.dataAcquisitionProjectId);
              question.imageType = 'PNG';
              if (!instrument.pngFiles[questionNumber]) {
                JobLoggingService.error({
                  message: 'question-' +
                    'management.log-messages.question.not-found-image-file',
                  messageParams: {
                    questionNumber: questionNumber,
                    instrument: instrument.instrumentName
                  },
                  objectType: 'question'
                });
              } else {
                questionResources.push(new QuestionResource(question));
              }
              resolve();
            } catch (e) {
              JobLoggingService.error({
                message: 'question-management.log-messages.' +
                  'question.unable-to-parse-json-file',
                messageParams: {
                  file: questionNumber + '.json',
                  instrument: instrument.instrumentName
                },
                objectType: 'question'
              });
              resolve();
            }
          }, function() {
            JobLoggingService.error({
              message: 'question-management.log-messages.' +
                'question.unable-to-read-file',
              messageParams: {
                file: questionNumber + '.json',
                instrument: instrument.instrumentName
              },
              objectType: 'question'
            });
            resolve();
          });
      });
    };

    var deleteAllQuestionsNotPresentInJson = function() {
      var promiseChain = $q.when();
      _.each(existingQuestions, function(existingQuestion) {
        if (!existingQuestion.presentInJson) {
          promiseChain = promiseChain.then(function() {
            return QuestionResource.delete({id: existingQuestion.id}).$promise
            .catch(
              function(error) {
                console.log('Error when deleting question:', error);
              });
          });
        }
      });
      return promiseChain;
    };

    var deleteAllImages = function(questionId) {
      var deferred = $q.defer();
      QuestionImageUploadService.deleteAllImages(questionId)
      .catch(function(error) {
        console.log('Unable to delete images:' + error);
      }).finally(function() {
        deferred.resolve();
      });
      return deferred.promise;
    };

    var uploadQuestion = function(question, image) {
      return $q(function(resolve) {
        question.$save().then(function() {
          JobLoggingService.success({
            objectType: 'question'
          });
          deleteAllImages(question.id).finally(function() {
            QuestionImageUploadService.uploadImage(image,
              question.id)
              .then(function() {
                JobLoggingService.success({
                  objectType: 'image'
                });
                resolve();
              }, function() {
                JobLoggingService.error({
                  message: 'question-management.log-messages.' +
                  'question.unable-to-upload-image-file',
                  messageParams: {
                    file: question.number + '.png'
                  },
                  objectType: 'image'
                });
                resolve();
              });
          });
        }, function(error) {
          var errorMessages = ErrorMessageResolverService
            .getErrorMessage(error, 'question');
          JobLoggingService.error({
            message: errorMessages.message,
            messageParams: errorMessages.translationParams,
            subMessages: errorMessages.subMessages,
            objectType: 'question'
          });
          resolve();
        });
      });
    };

    var uploadInstruments = function(instrumentIndex) {
      if (instrumentIndex === _.size(filesMap)) {
        deleteAllQuestionsNotPresentInJson().finally(function() {
          ElasticSearchAdminService.processUpdateQueue('questions').finally(
            function() {
            var job = JobLoggingService.getCurrentJob();
            JobLoggingService.finish(
              'question-management.log-messages.question.upload-terminated', {
                totalQuestions: job.getCounts('question').total,
                totalImages: job.getCounts('image').total,
                totalWarnings: job.warnings,
                totalErrors: job.errors
              }
            );
            $rootScope.$broadcast('upload-completed');
          });
        });
      } else {
        var instrument = _.filter(filesMap, function(filesObject) {
          return filesObject.instrumentIndex === instrumentIndex;
        })[0];
        questionResources = [];
        var chainedQuestionResourceBuilder = $q.when();
        _.forEach(instrument.jsonFiles, function(questionAsJson,
          questionNumber) {
          chainedQuestionResourceBuilder =
            chainedQuestionResourceBuilder
            .then(function() {
              return createQuestionResource(
                instrument, questionAsJson, questionNumber);
            });
        });
        chainedQuestionResourceBuilder.finally(
          function() {
            var chainedQuestionUploads = $q.when();
            questionResources.forEach(function(question) {
              chainedQuestionUploads = chainedQuestionUploads.then(
                function() {
                  return uploadQuestion(question,
                    instrument.pngFiles[question.number]);
                });
            });
            chainedQuestionUploads.finally(function() {
              uploadInstruments(instrumentIndex + 1);
            });
          });
      }
    };

    var startJob = function(files, dataAcquisitionProjectId) {
      JobLoggingService.start('question');
      createInstrumentsFileMap(files, dataAcquisitionProjectId);
      uploadInstruments(0);
    };

    var uploadQuestions = function(files, dataAcquisitionProjectId) {
      existingQuestions = {};
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        QuestionRepositoryClient.findByDataAcquisitionProjectId(
          dataAcquisitionProjectId).then(function(result) {
            result.data.forEach(function(question) {
              existingQuestions[question.id] = question;
            });
            if (result.data.length > 0) {
              var confirm = $mdDialog.confirm().title($translate.instant(
                  'search-management.delete-messages.delete-questions-title'))
                .textContent($translate.instant(
                  'search-management.delete-messages.delete-questions', {
                    id: dataAcquisitionProjectId
                  })).ariaLabel($translate.instant(
                  'search-management.delete-messages.delete-questions', {
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
      uploadQuestions: uploadQuestions
    };
  });
