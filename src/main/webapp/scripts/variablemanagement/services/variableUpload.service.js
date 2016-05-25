/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
function($translate, ZipReaderService,
  VariableBuilderService, VariableDeleteResource, JobLoggingService,
  ErrorMessageResolverService, ExcelReaderService) {
  var objects;
  var uploadCount;
  var upload = function() {
    if (uploadCount === objects.length) {
      JobLoggingService.finish($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.variable.uploadTerminated', {}));
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
      var excelFile = zipFile.files['variables.xlsx'];
      zip = zipFile;
      return ExcelReaderService.readFileAsync(excelFile);
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
          dataAcquisitionProjectId: dataAcquisitionProjectId},
          upload, function(error) {
            var errorMessage = ErrorMessageResolverService
            .getErrorMessage(error, 'variable');
            JobLoggingService.error(errorMessage);
          });
    }, function(error) {
      console.log(error);
      JobLoggingService.cancel($translate.instant(
        'metadatamanagementApp.dataAcquisitionProject.detail.' +
        'logMessages.unsupportedExcelFile', {}));
    });
  };
  return {
          uploadVariables: uploadVariables
        };
});
