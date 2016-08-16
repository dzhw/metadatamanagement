/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
  function(ZipReaderService,
    VariableBuilderService, VariableDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService) {
    var objects;
    var uploadCount;

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'variable-management.log-messages.variable.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
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
              .getErrorMessages(error, 'variable');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadVariables = function(file, dataAcquisitionProjectId) {
      var zip;
      uploadCount = 0;
      JobLoggingService.start('variable');
      ZipReaderService.readZipFileAsync(file)
        .then(function(zipFile) {
          try {
            var hasVariablesFolder = zipFile.files['variables/'].dir;
            var excelFile = zipFile.files['variables.xlsx'];
            if (hasVariablesFolder && excelFile) {
              zip = zipFile;
              return ExcelReaderService.readFileAsync(excelFile);
            } else {
              return $q.reject('unsupportedDirectoryStructure');
            }
          } catch (e) {
            return $q.reject('unsupportedDirectoryStructure');
          }
        }, function() {
          JobLoggingService.cancel(
            'global.log-messages.unsupported-zip-file', {});
        }).then(function(variables) {
          objects = VariableBuilderService.getVariables(variables, zip,
            dataAcquisitionProjectId);
          VariableBuilderService.getParseErrors
            .forEach(function(errorMessage) {
              JobLoggingService.error(errorMessage.errorMessage,
                errorMessage.translationParams);
            });
          VariableDeleteResource.deleteByDataAcquisitionProjectId({
              dataAcquisitionProjectId: dataAcquisitionProjectId
            },
            upload,
            function(error) {
              var errorMessages = ErrorMessageResolverService
                .getErrorMessages(error, 'variable');
              errorMessages.forEach(function(errorMessage) {
                JobLoggingService.error(errorMessage.message,
                  errorMessage.translationParams);
              });
            });
        }, function(error) {
          if (error === 'unsupportedDirectoryStructure') {
            JobLoggingService.cancel(
              'global.log-messages.unsupported-directory-structure', {});
          } else {
            JobLoggingService.cancel(
              'global.log-messages.unsupported-excel-file', {});
          }
        });
    };

    return {
      uploadVariables: uploadVariables
    };
  });
