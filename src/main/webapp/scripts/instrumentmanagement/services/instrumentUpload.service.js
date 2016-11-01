/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('InstrumentUploadService',
  function(InstrumentBuilderService, InstrumentDeleteResource,
    JobLoggingService, ErrorMessageResolverService, ExcelReaderService, $q,
    FileReaderService, ElasticSearchAdminService, $rootScope) {
    var objects;
    var uploadCount;

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'instrument-management.log-messages.instrument.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (objects[uploadCount] === null) {
          uploadCount++;
          return upload();
        } else if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'instrument-management.log-messages.instrument.missing-id', {
              index: index + 1
            });
          uploadCount++;
          return upload();
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success();
            uploadCount++;
            return upload();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'instrument');
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
      objects = [];
      JobLoggingService.start('instrument');
      InstrumentDeleteResource.deleteByDataAcquisitionProjectId({
        dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise.then(
        function() {
          var excelFile;
          var jsonFiles = {};
          var jsonFileReaders = [];

          files.forEach(function(file) {
            if (file.name === 'instruments.xlsx') {
              excelFile = file;
            }
            if (file.name.endsWith('.json')) {
              var instrumentId = file.name.substring(
                0, file.name.indexOf('.json'));
              jsonFiles[instrumentId] = file;
            }
          });

          if (!excelFile) {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'instruments.xlsx'});
            return;
          }

          ExcelReaderService.readFileAsync(excelFile).then(
            function(instruments) {
            instruments.forEach(function(instrumentFromExcel) {
              if (jsonFiles[instrumentFromExcel.id]) {
                jsonFileReaders.push(FileReaderService.readAsText(
                  jsonFiles[instrumentFromExcel.id]).then(
                    function(instrumentAsText) {
                    try {
                      console.log(instrumentAsText);
                      objects.push(InstrumentBuilderService.buildInstrument(
                        instrumentFromExcel, dataAcquisitionProjectId));
                    } catch (e) {
                      JobLoggingService.error(
                        'global.log-messages.unable-to-parse-json-file',
                        {file: jsonFiles[instrumentFromExcel.id].name});
                    }
                  }), function() {
                    JobLoggingService.error(
                      'global.log-messages.unable-to-read-file',
                      {file: jsonFiles[instrumentFromExcel.id].name});
                  });
              } else {
                JobLoggingService.error(
                  'instrument-management.log-messages.instrument.' +
                  'missing-json-file',
                  {id: instrumentFromExcel.id});
              }
            });
          }, function() {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'instruments.xlsx'});
            return $q.reject();
          }).then(function() {
              return $q.all(jsonFileReaders);
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
