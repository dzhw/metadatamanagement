/*jshint loopfunc: true */
/* global Blob */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('QuestionUploadService',
  function(FileReaderService, QuestionResource, QuestionDeleteResource,
    JobLoggingService, QuestionImageUploadService, CleanJSObjectService,
    ErrorMessageResolverService, $q, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog) {
      var filesMap;
      var questionResources;
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
              filesMap[path[pathLength - 2]].instrumentIndex = instrumentIndex;
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
              filesMap[path[pathLength - 3]].instrumentIndex = instrumentIndex;
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
                      question.instrumentId = instrument
                      .dataAcquisitionProjectId + '-' +
                      instrument.instrumentName;
                      question.instrumentNumber = instrument.instrumentNumber;
                      question.id = question.instrumentId + '-' +
                      questionNumber;
                      question.number = questionNumber;
                      var successors = [];
                      if (!CleanJSObjectService.isNullOrEmpty(question
                        .successorNumbers)) {
                        question.successorNumbers
                        .forEach(function(successorNumber) {
                          successors.push(question.instrumentId + '-' +
                          successorNumber);
                        });
                      }
                      question.successors = successors;
                      question.imageType = 'PNG';
                      if (!instrument.pngFiles[questionNumber]) {
                        JobLoggingService.error({message: 'question-' +
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

      var uploadQuestion = function(question, image) {
        return $q(function(resolve) {
          question.$save().then(function() {
            JobLoggingService.success({
                objectType: 'question'
              });
            var imageFile = new Blob([image], {
              type: 'image/png'
            });
            QuestionImageUploadService.uploadImage(imageFile, question.id)
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
          ElasticSearchAdminService.processUpdateQueue().finally(function() {
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
          return;
        } else {
          var instrument = _.filter(filesMap, function(filesObject) {
            return filesObject.instrumentIndex === instrumentIndex;
          })[0];
          questionResources = [];
          var chainedQuestionResourceBuilder = $q.when();
          _.forEach(instrument.jsonFiles, function(questionAsJson,
            questionNumber) {
              chainedQuestionResourceBuilder = chainedQuestionResourceBuilder
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
      var uploadQuestions = function(files, dataAcquisitionProjectId) {
        if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
          var confirm = $mdDialog.confirm().title($translate.instant(
            'search-management.delete-messages.delete-questions-title'))
            .textContent($translate.instant(
              'search-management.delete-messages.delete-questions', {
                id: dataAcquisitionProjectId})).ariaLabel($translate.instant(
                  'search-management.delete-messages.delete-questions',
                  {id: dataAcquisitionProjectId}))
                  .ok($translate.instant('global.buttons.ok'))
                  .cancel($translate.instant('global.buttons.cancel'));
          $mdDialog.show(confirm).then(function() {
                JobLoggingService.start('question');
                QuestionDeleteResource.deleteByDataAcquisitionProjectId({
                  dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise
                  .then(function() {
                    createInstrumentsFileMap(files, dataAcquisitionProjectId);
                    console.log(filesMap);
                    uploadInstruments(0);
                  }, function() {
                      JobLoggingService.cancel(
                      'question-management.log-messages.question.' +
                      'unable-to-delete', {});
                    });
              });
        }};
      return {
        uploadQuestions: uploadQuestions
      };
    });
