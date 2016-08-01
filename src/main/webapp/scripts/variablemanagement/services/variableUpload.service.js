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
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.variable.uploadTerminated', {
              total: objects.length,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.variable.' +
            'missingId', {
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
            //TODO DKatzberg
            //Das Problem liegt hier. Error hat schon ein übersetzten Wert...
            //Der muss noch entfernt werden... Woher kommt error?
            //Lösung: Backend müssen bei den messages, die geschweiften Klammern
            //weg. Dann werden die Entsprechen korrekt rüber geschickt.
            //Und die String sind im Backend in den I18n String notiert. Diese
            //müssen nun rüber geholt werden in den Frontend I18n
            //Das gilt für alle Validierungsfehler, zb Distribution.java
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'variable');
            errorMessages.forEach(function(errorMessage) {
              JobLoggingService.error(errorMessage.message,
                errorMessage.translationParams);
            });
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
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.unsupportedZipFile', {});
        }).then(function(variables) {
          objects = VariableBuilderService.getVariables(variables, zip,
            dataAcquisitionProjectId);
          VariableBuilderService.getParseErrors
          .forEach(function(errorMessage, translationParams) {
            JobLoggingService.error(errorMessage, translationParams);
          }, VariableBuilderService.getParseErrors);
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
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.unsupportedDirectoryStructure', {});
          } else {
            JobLoggingService.cancel(
              'metadatamanagementApp.dataAcquisitionProject.detail.' +
              'logMessages.unsupportedExcelFile', {});
          }
        });
    };

    return {
      uploadVariables: uploadVariables
    };
  });
