/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('StudyUploadService',
  function(ZipReaderService,
    StudyBuilderService, StudyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService) {
    var objects;
    var uploadCount;

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().then(function() {
          JobLoggingService.finish(
            'study-management.log-messages.studies.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'study-management.log-messages.studies.missing-id', {
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
              .getErrorMessages(error, 'studies');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadStudies = function(file, dataAcquisitionProjectId) {
      var zip;
      uploadCount = 0;
      JobLoggingService.start('studies');
      ZipReaderService.readZipFileAsync(file)
        .then(function(zipFile) {
          try {
            var excelFileStudy = zipFile.files['study.xlsx'];
            var excelFileReleases = zipFile.files['releases.xlsx'];
            if (excelFileReleases && excelFileStudy) {
              zip = zipFile;
              var study = ExcelReaderService.readFileAsync(excelFileStudy);
              study.releases =
                ExcelReaderService.readFileAsync(excelFileReleases);
              return study;
            } else {
              return $q.reject('unsupportedDirectoryStructure');
            }
          } catch (e) {
            return $q.reject('unsupportedDirectoryStructure');
          }
        }, function() {
          JobLoggingService.cancel(
            'global.log-messages.unsupported-zip-file', {});
        }).then(function(study) {
          objects = StudyBuilderService.getStudies(study, zip,
            dataAcquisitionProjectId);
          StudyBuilderService.getParseErrors
            .forEach(function(errorMessage) {
              JobLoggingService.error(errorMessage.errorMessage,
                errorMessage.translationParams);
            });
          StudyDeleteResource.deleteByDataAcquisitionProjectId({
              dataAcquisitionProjectId: dataAcquisitionProjectId
            },
            upload,
            function(error) {
              var errorMessages = ErrorMessageResolverService
                .getErrorMessages(error, 'study');
              errorMessages.forEach(function(errorMessage) {
                JobLoggingService.error(errorMessage.message,
                  errorMessage.translationParams);
              });
            });
        }, function(error) {
          if (error === 'unsupportedDirectoryStructure') {
            JobLoggingService.cancel(
              'global.log-messages.unsupported-directory-structure', {}
            );
          } else {
            JobLoggingService.cancel(
              'global.log-messages.unsupported-excel-file', {});
          }
        });
    };

    return {
      uploadStudies: uploadStudies
    };
  });
