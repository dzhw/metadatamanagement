/*jshint loopfunc: true */
/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('InstrumentUploadService',
  function(InstrumentBuilderService, InstrumentRepositoryClient,
    JobLoggingService, ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService, $rootScope, InstrumentResource,
    InstrumentAttachmentUploadService, $translate, $mdDialog,
    CleanJSObjectService) {
    //array holding all instrument resources
    var instrumentsToSave;
    //a map instrumentNumber -> (map filename -> (attachment, metadata))
    var attachmentsToUpload;
    var uploadCount;
    //a map instrumentNumber -> true
    var previouslyUploadedInstrumentNumbers;
    // map instrumentId -> presentInExcel true/false
    var existingInstruments = {};

    var deleteAllInstrumentsNotPresentInExcel = function() {
      var promiseChain = $q.when();
      _.each(existingInstruments, function(existingInstrument) {
        if (!existingInstrument.presentInExcel) {
          promiseChain = promiseChain.then(function() {
            return InstrumentResource.delete({id: existingInstrument.id})
            .$promise.catch(
              function(error) {
                console.log('Error when deleting instrument:', error);
              });
          });
        }
      });
      return promiseChain;
    };

    var deleteAttachments = function(instrumentId) {
      var deferred = $q.defer();
      InstrumentAttachmentUploadService.deleteAllAttachments(instrumentId)
      .catch(function(error) {
        console.log('Unable to delete attachments:' + error);
      }).finally(function() {
        deferred.resolve();
      });
      return deferred.promise;
    };

    var upload = function() {
      if (uploadCount === instrumentsToSave.length) {
        deleteAllInstrumentsNotPresentInExcel().finally(function() {
          ElasticSearchAdminService.processUpdateQueue('instruments').finally(
            function() {
              var job = JobLoggingService.getCurrentJob();
              JobLoggingService.finish(
                'instrument-management.log-messages.instrument.' +
                'upload-terminated', {
                  totalInstruments: job.getCounts('instrument').total,
                  totalAttachments: job.getCounts('instrument-attachment')
                  .total,
                  totalWarnings: job.warnings,
                  totalErrors: job.errors
                }
              );
              $rootScope.$broadcast('upload-completed');
            });
        });
      } else {
        if (instrumentsToSave[uploadCount] === null) {
          uploadCount++;
          return upload();
        } else if (!instrumentsToSave[uploadCount].number ||
          instrumentsToSave[uploadCount].number === '') {
          // instrument does not have an id
          var index = uploadCount;
          JobLoggingService.error({
            message: 'instrument-management.log-messages' +
              '.instrument.missing-number',
            messageParams: {
              index: index + 1
            },
            objectType: 'instrument'
          });
          uploadCount++;
          return upload();
        } else if (previouslyUploadedInstrumentNumbers[
            instrumentsToSave[uploadCount].number]) {
          // duplicate instrument number
          JobLoggingService.error({
            message: 'instrument-management.log-messages' +
              '.instrument.duplicate-instrument-number',
            messageParams: {
              index: uploadCount + 1,
              number: instrumentsToSave[uploadCount].number
            },
            objectType: 'instrument'
          });
          uploadCount++;
          return upload();
        } else {
          instrumentsToSave[uploadCount].$save().then(function() {
            JobLoggingService.success({
              objectType: 'instrument'
            });
            // remember the successfully uploaded instrument number
            previouslyUploadedInstrumentNumbers[
              instrumentsToSave[uploadCount].number] = true;
            deleteAttachments(instrumentsToSave[uploadCount].id).then(
              function() {
              // get the map filename -> {attachment, metadata}
              var attachments = attachmentsToUpload[
                instrumentsToSave[uploadCount].number];
              //upload attachments if there are some
              if (attachments) {
                //upload all attachments sequentially
                var sequentialChain = $q.when();
                var fileNames = Object.keys(attachments);
                fileNames.forEach(function(fileName) {
                  sequentialChain = sequentialChain.then(function() {
                    return InstrumentAttachmentUploadService.uploadAttachment(
                      attachments[fileName].attachment,
                      attachments[fileName].metadata).then(
                      function() {
                        JobLoggingService.success({
                          objectType: 'instrument-attachment'
                        });
                      },
                      function(error) {
                        // attachment upload failed
                        var errorMessage =
                          ErrorMessageResolverService
                          .getErrorMessage(error, 'instrument',
                            'instrument-attachment', fileName);
                        JobLoggingService.error({
                          message: errorMessage.message,
                          messageParams: errorMessage.translationParams,
                          subMessages: errorMessage.subMessages,
                          objectType: 'instrument-attachment'
                        });
                      });
                  });
                });
                sequentialChain.finally(function() {
                  // finished attachment upload => continue with next instrument
                  uploadCount++;
                  return upload();
                });
              } else {
                //no attachments to upload => continue with next instrument
                uploadCount++;
                return upload();
              }
            });
          }).catch(function(error) {
            // instrument upload failed
            var errorMessages = ErrorMessageResolverService
              .getErrorMessage(error, 'instrument');

            if (errorMessages.subMessages.length > 0) {
              for (var i = 0; i < errorMessages.subMessages.length; ++i) {
                //+2, one line, because it starts at zero
                //the second addiional line is because of the
                //headline in the excel
                errorMessages.subMessages[i].translationParams.index =
                  uploadCount + 2;
              }
            }

            JobLoggingService.error({
              message: errorMessages.message,
              messageParams: errorMessages.translationParams,
              subMessages: errorMessages.subMessages,
              objectType: 'instrument'
            });
            uploadCount++;
            return upload();
          });
        }
      }
    };

    var startJob = function(files, dataAcquisitionProjectId) {
      // reset the instrument upload count
      uploadCount = 0;
      // reset the array of instrument resources
      instrumentsToSave = [];
      // reset the map instrumentId ->
      // (map filename -> (attachment, metadata))
      attachmentsToUpload = {};
      // reset the map instrumentNumber -> true
      previouslyUploadedInstrumentNumbers = {};
      JobLoggingService.start('instrument');

      // the excel file containing instruments and attachment metadata
      var excelFile;
      // map filename -> file
      var attachmentFiles = {};

      files.forEach(function(file) {
        if (file.name === 'instruments.xlsx') {
          excelFile = file;
        } else if ((file.path &&
            file.path.indexOf('attachments') > -1) ||
          (file.webkitRelativePath &&
            file.webkitRelativePath.indexOf('attachments') >
            -1)) {
          attachmentFiles[file.name] = file;
        }
      });

      if (!excelFile) {
        JobLoggingService.cancel(
          'global.log-messages.unable-to-read-file', {
            file: 'instruments.xlsx'
          });
        return;
      }

      ExcelReaderService.readFileAsync(excelFile, true).then(
        function(excelContent) {
          var instruments = excelContent.instruments;
          var attachments = excelContent.attachments;
          if (instruments) {
            instruments.forEach(function(
              instrumentFromExcel) {
              var instrument = InstrumentBuilderService.buildInstrument(
                instrumentFromExcel,
                dataAcquisitionProjectId);
              instrumentsToSave.push(instrument);
              if (existingInstruments[instrument.id]) {
                existingInstruments[instrument.id].presentInExcel =
                  true;
              }
            });
          } else {
            JobLoggingService.cancel(
              'global.log-messages.unable-to-read-excel-sheet', {
                sheet: 'instruments'
              });
            return $q.reject();
          }

          if (attachments) {
            attachments.forEach(function(metadataFromExcel,
              index) {
              // ensure that the attachment has an instrument number
              if (!metadataFromExcel.instrumentNumber) {
                JobLoggingService.error({
                  message: 'instrument-management.log-messages' +
                    '.instrument-attachment.missing-instrument-number',
                  messageParams: {
                    index: index + 1
                  },
                  objectType: 'instrument-attachment'
                });
                return;
              }
              // ensure that there is an instrument with the given
              // number
              if (!_.find(instrumentsToSave, function(
                  instrument) {
                  return instrument.number ===
                    metadataFromExcel.instrumentNumber;
                })) {
                JobLoggingService.error({
                  message: 'instrument-management.log-messages' +
                    '.instrument-attachment.unknown-instrument-number',
                  messageParams: {
                    index: index + 1
                  },
                  objectType: 'instrument-attachment'
                });
                return;
              }
              if (!metadataFromExcel.filename) {
                JobLoggingService.error({
                  message: 'instrument-management.log-messages.' +
                    'instrument-attachment.missing-filename',
                  messageParams: {
                    index: index + 1
                  },
                  objectType: 'instrument-attachment'
                });
                return;
              }
              if (!attachmentFiles[metadataFromExcel.filename]) {
                JobLoggingService.error({
                  message: 'instrument-management.log-messages.' +
                    'instrument-attachment.file-not-found',
                  messageParams: {
                    filename: metadataFromExcel.filename
                  },
                  objectType: 'instrument-attachment'
                });
                return;
              }
              if (!attachmentsToUpload[
                  metadataFromExcel
                  .instrumentNumber]) {
                attachmentsToUpload[metadataFromExcel
                  .instrumentNumber] = {};
              }
              if (!attachmentsToUpload[
                  metadataFromExcel
                  .instrumentNumber]
                [metadataFromExcel.filename]) {
                attachmentsToUpload[metadataFromExcel.instrumentNumber]
                  [metadataFromExcel.filename] = {};
              }
              attachmentsToUpload[metadataFromExcel.instrumentNumber]
                [metadataFromExcel.filename].metadata =
                InstrumentBuilderService
                .buildInstrumentAttachmentMetadata(
                  metadataFromExcel,
                  dataAcquisitionProjectId, index);
              attachmentsToUpload[metadataFromExcel.instrumentNumber]
                [metadataFromExcel.filename].attachment =
                attachmentFiles[metadataFromExcel.filename];
            });
          } else {
            JobLoggingService.cancel(
              'global.log-messages.unable-to-read-excel-sheet', {
                sheet: 'attachments'
              });
            return $q.reject();
          }
        },
        function() {
          JobLoggingService.cancel(
            'global.log-messages.unable-to-read-file', {
              file: 'instruments.xlsx'
            });
          return $q.reject();
        }).then(upload);
    };

    // upload instruments for the given projects
    var uploadInstruments = function(files, dataAcquisitionProjectId) {
      existingInstruments = {};
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        InstrumentRepositoryClient.findByDataAcquisitionProjectId(
          dataAcquisitionProjectId).then(function(result) {
            result.data.forEach(function(instrument) {
              existingInstruments[instrument.id] = instrument;
            });
            if (result.data.length > 0) {
              var confirm = $mdDialog.confirm()
                .title($translate.instant(
                  'search-management.delete-messages.delete-instruments-title'))
                .textContent($translate.instant(
                  'search-management.delete-messages.delete-instruments', {
                    id: dataAcquisitionProjectId
                  }))
                .ariaLabel($translate.instant(
                  'search-management.delete-messages.delete-instruments', {
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
      uploadInstruments: uploadInstruments
    };
  });
