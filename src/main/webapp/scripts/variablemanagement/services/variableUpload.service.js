/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
  function($translate, ZipReaderService,
    VariableBuilderService, VariableDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService) {
    var objects;
    var uploadCount;

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.uploadTerminated', {
              total: objects.length,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.variable.' +
            'missingId', {
              index: index + 1
            }));
          uploadCount++;
          return upload();
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success();
            uploadCount++;
            return upload();
          }).catch(function(error) {
            var errorMessage = ErrorMessageResolverService
              .getErrorMessage(error, 'variable');
            JobLoggingService.error(errorMessage);
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
        }, function(error) {
          console.log(error);
          JobLoggingService.cancel($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.unsupportedZipFile', {}));
        }).then(function(variables) {
          objects = VariableBuilderService.getVariables(variables, zip,
            dataAcquisitionProjectId);
          VariableBuilderService.getParseErrors.forEach(function(message) {
            JobLoggingService.error(message);
          });
          VariableDeleteResource.deleteByDataAcquisitionProjectId({
              dataAcquisitionProjectId: dataAcquisitionProjectId
            },
            upload,
            function(error) {
              var errorMessage = ErrorMessageResolverService
                .getErrorMessage(error, 'variable');
              JobLoggingService.error(errorMessage);
            });
        }, function(error) {
          if (error === 'unsupportedDirectoryStructure') {
            JobLoggingService.cancel($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.unsupportedDirectoryStructure', {}));
          } else {
            console.log(error);
            JobLoggingService.cancel($translate.instant(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.unsupportedExcelFile', {}));
          }
        });
    };

    return {
      uploadVariables: uploadVariables
    };
  });
