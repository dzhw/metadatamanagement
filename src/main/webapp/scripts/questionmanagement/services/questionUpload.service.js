/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('QuestionUploadService',
  function(FileReaderService, QuestionResource, QuestionRepositoryClient,
    JobLoggingService, QuestionImageUploadService, CleanJSObjectService,
    ErrorMessageResolverService, $q, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog, QuestionIdBuilderService, StudyIdBuilderService,
    InstrumentIdBuilderService, Upload) {
    var filesMap;
    var questionImageMetadataResources;
    var questionImageArrayByQuestionNumber;
    // map questionId -> presentInJson true/false
    var existingQuestions = {};
    var usedIndexInInstrument = {};
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
        //Question Json File in the basic path
        if (_.endsWith(fileName, '.json') && pathLength === 3) {
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
            filesMap[path[pathLength - 2]].jsonFilesForImages = {};
            instrumentIndex++;
          }
          filesMap[path[pathLength - 2]]
            .jsonFiles[_.split(fileName, '.json')[0]] = file;
        }
        //Image file
        if (pathLength === 5) {
          if (_.endsWith(fileName, '.png')) {
            if (!filesMap[path[pathLength - 4]]) {
              filesMap[path[pathLength - 4]] = {};
              filesMap[path[pathLength - 4]].dataAcquisitionProjectId =
                dataAcquisitionProjectId;
              filesMap[path[pathLength - 4]].instrumentName =
                path[pathLength - 4];
              filesMap[path[pathLength - 4]].instrumentNumber =
                _.split(path[pathLength - 4], 'ins')[1];
              filesMap[path[pathLength - 4]].instrumentIndex =
                instrumentIndex;
              filesMap[path[pathLength - 4]].questionNumber =
                path[pathLength - 2];
              filesMap[path[pathLength - 4]].pngFiles = {};
              filesMap[path[pathLength - 4]].jsonFiles = {};
              filesMap[path[pathLength - 4]].jsonFilesForImages = {};
              instrumentIndex++;
            }
            filesMap[path[pathLength - 4]]
              .pngFiles[_.split(fileName, '.png')[0]] = file;
            filesMap[path[pathLength - 4]]
              .pngFiles[_.split(fileName, '.png')[0]].questionNumber =
              path[pathLength - 2];
          }
          //Specific json file for the image
          //Json File for images has Metadata for the image
          if (_.endsWith(fileName, '.json')) {
            if (!filesMap[path[pathLength - 4]]) {
              filesMap[path[pathLength - 4]] = {};
              filesMap[path[pathLength - 4]].dataAcquisitionProjectId =
                dataAcquisitionProjectId;
              filesMap[path[pathLength - 4]].instrumentName =
                path[pathLength - 4];
              filesMap[path[pathLength - 4]].instrumentNumber =
                _.split(path[pathLength - 4], 'ins')[1];
              filesMap[path[pathLength - 4]].instrumentIndex =
                instrumentIndex;
              filesMap[path[pathLength - 4]].questionNumber =
                path[pathLength - 2];
              filesMap[path[pathLength - 4]].pngFiles = {};
              filesMap[path[pathLength - 4]].jsonFiles = {};
              filesMap[path[pathLength - 4]].jsonFilesForImages = {};
              instrumentIndex++;
            }
            filesMap[path[pathLength - 4]]
              .jsonFilesForImages[_.split(fileName, '.json')[0]] = file;
            filesMap[path[pathLength - 4]]
              .jsonFilesForImages[_.split(fileName, '.json')[0]]
              .questionNumber = path[pathLength - 2];
          }
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
              resolve(new QuestionResource(question));
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

    var createQuestionImageMetadataResource = function(
      questionImageJson, image, question, property) {
      return $q(function(resolve) {

          if (!image || !questionImageJson) {
            if (!image && !questionImageJson) {
              JobLoggingService.error({
                message: 'question-management.log-messages.' +
                  'question-image-metadata.not-found-image-and-metadata-file',
                messageParams: {
                  questionNumber: question.number,
                  instrument: question.instrumentNumber,
                  imageFilename: property + '.png',
                  metadataFilename: property + '.json'
                },
                objectType: 'questionImageMetadata'
              });
            } else {
              if (!image) {
                JobLoggingService.error({
                  message: 'question-management.log-messages.' +
                    'question-image-metadata.not-found-image-file',
                  messageParams: {
                    questionNumber: question.number,
                    instrument: question.instrumentNumber,
                    imageFilename: property + '.png',
                    metadataFilename: property + '.json'
                  },
                  objectType: 'questionImageMetadata'
                });
              }

              if (!questionImageJson) {
                JobLoggingService.error({
                  message: 'question-management.log-messages.' +
                    'question-image-metadata.not-found-metadata-file',
                  messageParams: {
                    questionNumber: question.number,
                    instrument: question.instrumentNumber,
                    imageFilename: property + '.png',
                    metadataFilename: property + '.json'
                  },
                  objectType: 'questionImageMetadata'
                });
              }
            }
            resolve();
          } else {
            FileReaderService.readAsText(questionImageJson)
              .then(function(result) {
                try {
                  var questionImageMetadata = CleanJSObjectService
                    .removeEmptyJsonObjects(JSON.parse(result));
                  questionImageMetadata.dataAcquisitionProjectId =
                      question.dataAcquisitionProjectId;
                  questionImageMetadata.questionId = question.id;
                  questionImageMetadata.fileName = image.name;
                  questionImageMetadata.imageType = 'PNG';
                  questionImageMetadata.resolution = {};

                  // Get image file dimensions
                  Upload.imageDimensions(image).then(function(dimensions) {
                    questionImageMetadata.resolution.widthX = dimensions.width;
                    questionImageMetadata.resolution.heightY =
                      dimensions.height;
                  });

                  //if no resolution -> error message
                  if (CleanJSObjectService.isNullOrEmpty(
                      questionImageMetadataResources[question.number])) {
                    questionImageMetadataResources[question.number] = [];
                  }
                  questionImageMetadataResources[question.number]
                    .push(questionImageMetadata);
                  resolve();
                } catch (e) {
                  JobLoggingService.error({
                    message: 'question-management.log-messages.' +
                      'question-image-metadata.unable-to-parse-json-file',
                    messageParams: {
                      file: questionImageJson.name,
                      questionNumber: question.number
                    },
                    objectType: 'questionImageMetadata'
                  });
                  resolve();
                }
              }, function() {
                JobLoggingService.error({
                  message: 'question-management.log-messages.' +
                    'question-image-metadata.unable-to-read-file',
                  messageParams: {
                    file: questionImageJson.name,
                    questionNumber: question.number
                  },
                  objectType: 'questionImageMetadata'
                });
                resolve();
              });
          }
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

    var deleteAllImages = function(questionImageMetadata) {
      var deferred = $q.defer();
      QuestionImageUploadService.deleteAllImages(questionImageMetadata)
      .catch(function(error) {
        console.log('Unable to delete images:' + error);
      }).finally(function() {
        deferred.resolve();
      });
      return deferred.promise;
    };

    var uploadQuestion = function(question, images, questionImageMetadataList) {
      question.$save().then(function() {
        JobLoggingService.success({
          objectType: 'question'
        });

        if (CleanJSObjectService
          .isNullOrEmpty(usedIndexInInstrument[question.instrumentId])) {
          usedIndexInInstrument[question.instrumentId] = {};
        }

        if (CleanJSObjectService
          .isNullOrEmpty(usedIndexInInstrument[question.instrumentId]
            [question.indexInInstrument])) {
          usedIndexInInstrument[question.instrumentId]
            [question.indexInInstrument] = question.id;
        } else {
          JobLoggingService.warning({
            message: 'question-management.log-messages.' +
              'question.non-unique-index-in-instrument',
            messageParams: {
              index: question.indexInInstrument,
              firstQuestionId:
                usedIndexInInstrument[question.instrumentId]
                  [question.indexInInstrument],
              secondQuestionId: question.id
            }
          });
        }

        if (CleanJSObjectService.isNullOrEmpty(questionImageMetadataList)) {
          JobLoggingService.error({
            message: 'question-management.log-messages.' +
              'question-image-metadata.not-depending-image-metadata',
            messageParams: {
              questionNumber: question.number,
              instrument: question.instrumentNumber
            },
            objectType: 'image'
          });
        } else {
          questionImageMetadataList.forEach(function(questionImageMetadata) {
              deleteAllImages(questionImageMetadata).finally(function() {
                var image = images[questionImageMetadata.fileName];
                QuestionImageUploadService.uploadImage(image,
                  questionImageMetadata)
                  .then(function() {
                    JobLoggingService.success({
                      objectType: 'image'
                    });
                  }, function() {
                    JobLoggingService.error({
                      message: 'question-management.log-messages.' +
                      'question.unable-to-upload-image-file',
                      messageParams: {
                        file: image.name
                      },
                      objectType: 'image'
                    });
                  });
              });
            });
        }
      }, function(error) {
        var errorMessages = ErrorMessageResolverService
          .getErrorMessage(error, 'question');
        JobLoggingService.error({
          message: errorMessages.message,
          messageParams: errorMessages.translationParams,
          subMessages: errorMessages.subMessages,
          objectType: 'question'
        });
      });
    };

    var createQuestionUploadChain = function(instrument) {
      questionImageMetadataResources = {};
      questionImageArrayByQuestionNumber = {};
      var question = {};
      var chainedQuestionUploads = $q.when();
      _.forEach(instrument.jsonFiles, function(questionAsJson,
        questionNumber) {
        chainedQuestionUploads = chainedQuestionUploads
        //Build Question Resource
        .then(function() {
          return createQuestionResource(
            instrument, questionAsJson, questionNumber);
        })
        //Build Question Image Resource
        .then(function(questionCreated) {
          question = questionCreated;
          var chainedQuestionImageMetadataResourceBuilder = $q.when();
          Object.keys(instrument.jsonFilesForImages)
            .forEach(function(property) {
              if (instrument.jsonFilesForImages[property].questionNumber ===
                question.number) {
                chainedQuestionImageMetadataResourceBuilder =
                    chainedQuestionImageMetadataResourceBuilder
                  .then(function() {
                      return createQuestionImageMetadataResource(
                        instrument.jsonFilesForImages[property],
                        instrument.pngFiles[property],
                        question, property);
                    })
                  .then(function() {
                    if (instrument.pngFiles
                      .hasOwnProperty(property)) {
                      if (CleanJSObjectService.isNullOrEmpty(
                          questionImageArrayByQuestionNumber
                          [question.number])) {
                        questionImageArrayByQuestionNumber
                        [question.number] = {};
                      }
                      var mapForQuestionImages =
                        questionImageArrayByQuestionNumber[question.number];
                      mapForQuestionImages
                        [instrument.pngFiles[property].name] =
                        instrument.pngFiles[property];
                    }
                  });
              }
            });
        })
        //Upload Question
        .then(function() {
            return uploadQuestion(question,
              questionImageArrayByQuestionNumber[question.number],
              questionImageMetadataResources[question.number]);
          });
      });

      return chainedQuestionUploads;
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
        createQuestionUploadChain(instrument).finally(function() {
            uploadInstruments(instrumentIndex + 1);
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
      usedIndexInInstrument = {};
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
