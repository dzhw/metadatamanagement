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
          JobLoggingService.finish(
            'instrument-management.log-messages.instrument.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (instrumentsToSave[uploadCount] === null) {
          uploadCount++;
          return upload();
        } else if (!instrumentsToSave[uploadCount].id ||
          instrumentsToSave[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'instrument-management.log-messages.instrument.missing-id', {
              index: index + 1
            });
          uploadCount++;
          return upload();
        } else {
          instrumentsToSave[uploadCount].$save().then(function() {
            JobLoggingService.success();
            var sequentialChain = $q.when();
            var fileNames = Object.keys(attachmentsToUpload[
              instrumentsToSave[uploadCount].id]);
            fileNames.forEach(function(fileName) {
              sequentialChain = sequentialChain.then(function() {
                return InstrumentAttachmentUploadService.uploadAttachment(
                  attachmentsToUpload[instrumentsToSave[uploadCount].id]
                  [fileName].attachment,
                  attachmentsToUpload[instrumentsToSave[uploadCount].id]
                  [fileName].metadata);
              }).then(function() {
                  console.log('success');
                }).catch(function(error) {
                  if (error === 'previouslyHandledError') {
                    console.log('success');
                  } else {
                    //TODO log error
                    console.log('error');
                  }
                  return $q.reject('previouslyHandledError');
                });
            });
            sequentialChain.finally(function() {
              uploadCount++;
              return upload();
            });
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessage(error, 'instrument');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
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
