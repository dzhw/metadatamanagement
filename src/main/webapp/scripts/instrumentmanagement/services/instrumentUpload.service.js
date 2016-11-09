/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('InstrumentUploadService',
  function(InstrumentBuilderService, InstrumentDeleteResource,
    JobLoggingService, ErrorMessageResolverService, ExcelReaderService, $q,
    FileReaderService, ElasticSearchAdminService, $rootScope,
    InstrumentAttachmentUploadService) {
    var instrumentsToSave;
    var attachmentsToUpload;
    var uploadCount;

    var upload = function() {
      if (uploadCount === instrumentsToSave.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          var job = JobLoggingService.getCurrentJob();
          JobLoggingService.finish(
            'instrument-management.log-messages.instrument.upload-terminated',
            {
              totalInstruments: job.getCounts('instrument').total,
              totalAttachments:
                job.getCounts('instrument-attachment').total,
              totalErrors: job.errors
            }
          );
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (instrumentsToSave[uploadCount] === null) {
          uploadCount++;
          return upload();
        } else if (!instrumentsToSave[uploadCount].id ||
          instrumentsToSave[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error({
            message: 'instrument-management.log-messages.instrument.missing-id',
            messageParams: {index: index + 1},
            objectType: 'instrument'
          });
          uploadCount++;
          return upload();
        } else {
          instrumentsToSave[uploadCount].$save().then(function() {
            JobLoggingService.success({
              objectType: 'instrument'
            });
            var sequentialChain = $q.when();
            var fileNames = Object.keys(attachmentsToUpload[
              instrumentsToSave[uploadCount].id]);
            fileNames.forEach(function(fileName) {
              sequentialChain = sequentialChain.then(function() {
                return InstrumentAttachmentUploadService.uploadAttachment(
                  attachmentsToUpload[instrumentsToSave[uploadCount].id]
                  [fileName].attachment,
                  attachmentsToUpload[instrumentsToSave[uploadCount].id]
                  [fileName].metadata).then(function() {
                    JobLoggingService.success({
                      objectType: 'instrument-attachment'
                    });
                  } , function(error) {
                    var errorMessage = ErrorMessageResolverService
                    .getErrorMessage(error, 'instrument',
                    'instrument-attachment', fileName);
                    JobLoggingService.error({message: errorMessage.message,
                      messageParams: errorMessage.translationParams,
                      subMessages: errorMessage.subMessages,
                      objectType: 'instrument-attachment'
                    });
                  });
              });
            });
            sequentialChain.finally(function() {
              uploadCount++;
              return upload();
            });
          }).catch(function(error) {
            var errorMessage = ErrorMessageResolverService
              .getErrorMessage(error, 'instrument');
            JobLoggingService.error({message: errorMessage.message,
              messageParams: errorMessage.translationParams,
              subMessages: errorMessage.subMessages,
              objectType: 'instrument'
            });
            uploadCount++;
            return upload();
          });
        }
      }
    };

    var uploadInstruments = function(files, dataAcquisitionProjectId) {
      uploadCount = 0;
      instrumentsToSave = [];
      attachmentsToUpload = {};
      JobLoggingService.start('instrument');
      InstrumentDeleteResource.deleteByDataAcquisitionProjectId({
        dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise.then(
        function() {
          var excelFile;
          var attachmentFiles = {};

          files.forEach(function(file) {
            if (file.name === 'instruments.xlsx') {
              excelFile = file;
            } else if (file.path.startsWith('attachments')) {
              attachmentFiles[file.name] = file;
            }
          });

          if (!excelFile) {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'instruments.xlsx'});
            return;
          }

          ExcelReaderService.readFileAsync(excelFile, true).then(
            function(excelContent) {
            var instruments = excelContent.instruments;
            var attachments = excelContent.attachments;
            if (instruments) {
              instruments.forEach(function(instrumentFromExcel) {
                  instrumentsToSave.push(
                    InstrumentBuilderService.buildInstrument(
                    instrumentFromExcel, dataAcquisitionProjectId));
                });
            } else {
              JobLoggingService.cancel(
                'global.log-messages.unable-to-read-excel-sheet',
                {sheet: 'instruments'});
              return $q.reject();
            }

            if (attachments) {
              attachments.forEach(function(metadataFromExcel) {
                if (!metadataFromExcel.instrumentId) {
                  //TODO log error
                }
                if (!metadataFromExcel.filename) {
                  //TODO log error
                }
                if (!attachmentsToUpload[metadataFromExcel.instrumentId]) {
                  attachmentsToUpload[metadataFromExcel.instrumentId] = {};
                }
                if (!attachmentsToUpload[metadataFromExcel.instrumentId]
                  [metadataFromExcel.filename]) {
                  attachmentsToUpload[metadataFromExcel.instrumentId]
                  [metadataFromExcel.filename] = {};
                }
                attachmentsToUpload[metadataFromExcel.instrumentId]
                  [metadataFromExcel.filename].metadata =
                InstrumentBuilderService.buildInstrumentAttachmentMetadata(
                  metadataFromExcel, dataAcquisitionProjectId);
                attachmentsToUpload[metadataFromExcel.instrumentId]
                  [metadataFromExcel.filename].attachment =
                  attachmentFiles[metadataFromExcel.filename];
              });
            } else {
              JobLoggingService.cancel(
                'global.log-messages.unable-to-read-excel-sheet',
                {sheet: 'attachments'});
              return $q.reject();
            }

          }, function() {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'instruments.xlsx'});
            return $q.reject();
          }).then(upload);
        }, function() {
          JobLoggingService.cancel(
            'instrument-management.log-messages.instrument.unable-to-delete');
          return $q.reject();
        }
      );
    };

    return {
      uploadInstruments: uploadInstruments
    };
  });
