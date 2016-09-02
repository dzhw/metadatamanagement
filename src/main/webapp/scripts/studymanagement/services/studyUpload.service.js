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
            'study-management.log-messages.study.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'study-management.log-messages.study.missing-id', {
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
              .getErrorMessages(error, 'study');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadStudy = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('study');
      var zip;
      var releasesForStudy;
      ZipReaderService.readZipFileAsync(file)
        .then(function(zipFile) {
          try {
            zip = zipFile;
            var excelFileReleases = zipFile.files['releases.xlsx'];
            if (excelFileReleases) {
              return ExcelReaderService.readFileAsync(excelFileReleases);
            } else {
              return $q.reject('unsupportedDirectoryStructure');
            }
          } catch (e) {
            return $q.reject('unsupportedDirectoryStructure');
          }
        }, function() {
          JobLoggingService.cancel(
            'global.log-messages.unsupported-zip-file', {});
        }).then(function(releases) {
          try {
            releasesForStudy = releases;
            var excelFileStudy = zip.files['study.xlsx'];
            if (excelFileStudy) {
              return ExcelReaderService.readFileAsync(excelFileStudy);
            } else {
              return $q.reject('unsupportedDirectoryStructure');
            }
          } catch (e) {
            return $q.reject('unsupportedDirectoryStructure');
          }
        }, function() {
          JobLoggingService.cancel(
            'global.log-messages.unsupported-zip-file', {});
        })
        .then(function(study) {
          study.dataAcquisitionProjectId = dataAcquisitionProjectId;
          study.releases = releasesForStudy;
          objects = StudyBuilderService.getStudies(study);
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
      uploadStudy: uploadStudy
    };
  });
