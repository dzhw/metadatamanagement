/* global _, document */
'use strict';

angular.module('metadatamanagementApp').service('QuestionUploadService',
  function(FileReaderService, QuestionResource, QuestionRepositoryClient,
    JobLoggingService, QuestionImageUploadService, CleanJSObjectService,
    ErrorMessageResolverService, $q, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog, QuestionIdBuilderService,
    DataPackageIdBuilderService, InstrumentIdBuilderService, Upload, $timeout) {
    var filesMap;
    // map questionId -> presentInJson true/false
    var existingQuestions = {};
    var usedIndexInInstrument = {};

    var getPath = function(file) {
      var path;
      if (file.path) {
        path = _.split(file.path, '/');
      } else {
        if (file.webkitRelativePath) {
          path = _.split(file.webkitRelativePath, '/');
        }
      }
      return path;
    };

    var createInstrumentsFileMap = function(files, dataAcquisitionProjectId) {
      filesMap = {};
      var instrumentIndex = 0;
      var basicPathLength = 0;
      if (files.length >= 1) {
        basicPathLength = getPath(_.minBy(files, function(file) {
          return getPath(file).length;
        })).length;
      }
      files.forEach(function(file) {
        var path = getPath(file);
        var pathLength = path.length;
        var fileName = _.last(path);
        //Question Json File in the basic path
        if (_.endsWith(fileName, '.json') && pathLength === basicPathLength) {
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
        if (pathLength === (basicPathLength + 2)) {
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
              if (angular.isUndefined(question.masterId)) {
                question.masterId = question.id;
              }
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
              question.dataPackageId = DataPackageIdBuilderService
                .buildDataPackageId(question.dataAcquisitionProjectId);
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

    var getImageDimensions = function(image) {
      var deferred = $q.defer();
      Upload.dataUrl(image).then(function(dataUrl) {
        var img = angular.element('<img>').attr('src', dataUrl)
          .css('visibility', 'hidden').css('position', 'fixed')
          .css('max-width', 'none !important')
          .css('max-height', 'none !important');

        function success() {
          var width = img[0].naturalWidth || img[0].clientWidth;
          var height = img[0].naturalHeight || img[0].clientHeight;
          img.remove();
          image.$ngfWidth = width;
          image.$ngfHeight = height;
          deferred.resolve({width: width, height: height});
        }

        function error() {
          img.remove();
          deferred.reject('load error');
        }

        img.on('load', success);
        img.on('error', error);

        var secondsCounter = 0;
        function checkLoadErrorInCaseOfNoCallback() {
          $timeout(function() {
            if (img[0].parentNode) {
              if (img[0].clientWidth) {
                success();
              } else if (secondsCounter++ > 10) {
                error();
              } else {
                checkLoadErrorInCaseOfNoCallback();
              }
            }
          }, 1000);
        }

        checkLoadErrorInCaseOfNoCallback();

        angular.element(document.getElementsByTagName('body')[0]).append(img);
      }, function() {
        deferred.reject('load error');
      });
      return deferred.promise;
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
                  if (questionImageMetadata.resolution) {
                    resolve(questionImageMetadata);
                  } else {
                    questionImageMetadata.resolution = {};
                    // Get image file dimensions
                    getImageDimensions(image)
                    .then(function(dimensions) {
                      questionImageMetadata.resolution.widthX =
                        dimensions.width;
                      questionImageMetadata.resolution.heightY =
                        dimensions.height;
                      resolve(questionImageMetadata);
                    }).catch(function(error) {
                      console.log('Unable to detect image dimensions:' + error);
                      resolve(questionImageMetadata);
                    });
                  }
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

    var uploadQuestion = function(question, images, questionImageMetadataList) {
      return $q(function(resolve) {
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
            resolve();
          }

          if (CleanJSObjectService.isNullOrEmpty(questionImageMetadataList)) {
            deleteAllImages(question.id).finally(resolve);
          } else {
            deleteAllImages(question.id).finally(function() {
              var imageUploads = [];
              questionImageMetadataList.forEach(
                function(questionImageMetadata) {
                  var image = images[questionImageMetadata.fileName];
                  imageUploads.push(QuestionImageUploadService
                    .uploadImage(image, questionImageMetadata)
                    .then(function() {
                      JobLoggingService.success({
                        objectType: 'image'
                      });
                    }, function(error) {
                      var errorMessages = ErrorMessageResolverService
                        .getErrorMessage(error, 'question', 'question-image',
                        questionImageMetadata.fileName);
                      errorMessages.translationParams.instrument =
                        question.instrumentNumber;
                      JobLoggingService.error({
                        message: errorMessages.message,
                        messageParams: errorMessages.translationParams,
                        subMessages: errorMessages.subMessages,
                        objectType: 'image'
                      });
                    }));
                });
              $q.all(imageUploads).finally(resolve);
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
          resolve();
        });
      });
    };

    var createParallelQuestionUploadPromise = function(instrument) {
      var questionImageMetadataResources = {};
      var questionImageMap = {};
      var questionResourceMap = {};
      var questionUploadPromises = [];
      var chunkSize = 16;
      var questionNumbers = Object.keys(instrument.jsonFiles);
      for (var i = 0; i < chunkSize; i++) {
        questionUploadPromises.push($q.when());
      }
      questionNumbers.map(function(questionNumber, index) {
        var chainIndex = index % chunkSize;
        var promiseChain = questionUploadPromises[chainIndex];
        questionUploadPromises[chainIndex] = promiseChain.then(function() {
          return createQuestionResource(
            instrument, instrument.jsonFiles[questionNumber], questionNumber);
        })
          //Build Question Image Resource
        .then(function(question) {
          questionResourceMap[questionNumber] = question;
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
                  .then(function(questionImageMetadataResource) {
                    if (questionImageMetadataResource) {
                      if (CleanJSObjectService.isNullOrEmpty(
                        questionImageMetadataResources[questionNumber])) {
                        questionImageMetadataResources[questionNumber] = [];
                      }
                      questionImageMetadataResources[questionNumber]
                        .push(questionImageMetadataResource);

                      if (instrument.pngFiles.hasOwnProperty(property)) {
                        if (CleanJSObjectService.isNullOrEmpty(
                          questionImageMap[questionNumber])) {
                          questionImageMap[questionNumber] = {};
                        }

                        questionImageMap[questionNumber]
                          [instrument.pngFiles[property].name] =
                        instrument.pngFiles[property];
                      }
                    }
                  });
              }
            });
          return chainedQuestionImageMetadataResourceBuilder;
        }).then(function() {
          //Upload Question
          return uploadQuestion(questionResourceMap[questionNumber],
            questionImageMap[questionNumber],
            questionImageMetadataResources[questionNumber]);
        });
      });
      return $q.all(questionUploadPromises);
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
        createParallelQuestionUploadPromise(instrument).finally(function() {
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
      if (!files || files.length === 0) {
        return;
      }
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
