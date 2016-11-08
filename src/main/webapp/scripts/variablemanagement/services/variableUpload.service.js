/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
  function(VariableBuilderService, VariableDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q, FileReaderService,
    ElasticSearchAdminService, $rootScope) {
    var objects;
    var uploadCount;

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'variable-management.log-messages.variable.upload-terminated', {
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
            'variable-management.log-messages.variable.missing-id', {
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
              .getErrorMessage(error, 'variable');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };

    var uploadVariables = function(files, dataAcquisitionProjectId) {
      uploadCount = 0;
      objects = [];
      JobLoggingService.start('variable');
      VariableDeleteResource.deleteByDataAcquisitionProjectId({
        dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise.then(
        function() {
          var excelFile;
          var jsonFiles = {};
          var jsonFileReaders = [];

          files.forEach(function(file) {
            if (file.name === 'variables.xlsx') {
              excelFile = file;
            }
            if (file.name.endsWith('.json')) {
              var variableId = file.name.substring(
                0, file.name.indexOf('.json'));
              jsonFiles[variableId] = file;
            }
          });

          if (!excelFile) {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'variables.xlsx'});
            return;
          }

          ExcelReaderService.readFileAsync(excelFile).then(function(variables) {
            variables.forEach(function(variableFromExcel) {
              if (jsonFiles[variableFromExcel.id]) {
                jsonFileReaders.push(FileReaderService.readAsText(
                  jsonFiles[variableFromExcel.id]).then(
                    function(variableAsText) {
                    try {
                      var variableFromJson = JSON.parse(variableAsText);
                      objects.push(VariableBuilderService.buildVariable(
                        variableFromExcel, variableFromJson,
                        dataAcquisitionProjectId));
                    } catch (e) {
                      JobLoggingService.error(
                        'global.log-messages.unable-to-parse-json-file',
                        {file: jsonFiles[variableFromExcel.id].name});
                    }
                  }), function() {
                    JobLoggingService.error(
                      'global.log-messages.unable-to-read-file',
                      {file: jsonFiles[variableFromExcel.id].name});
                  });
              } else {
                JobLoggingService.error(
                  'variable-management.log-messages.variable.missing-json-file',
                  {id: variableFromExcel.id});
              }
            });
          }, function() {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'variables.xlsx'});
            return $q.reject();
          }).then(function() {
              return $q.all(jsonFileReaders);
            }).then(upload);
        }, function() {
          JobLoggingService.cancel(
            'variable-management.log-messages.variable.unable-to-delete');
          return $q.reject();
        }
      );
    };

    return {
      uploadVariables: uploadVariables
    };
  });
