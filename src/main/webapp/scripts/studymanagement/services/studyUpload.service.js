/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('StudyUploadService',
  function(StudyBuilderService, StudyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService, $rootScope) {
    var objects;
    var uploadCount;

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'study-management.log-messages.study.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
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

    var uploadStudy = function(files, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('study');
      var releasesFile;
      var studyFile;
      var releasesForStudy;
      files.forEach(function(file) {
        if (file.name === 'study.xlsx') {
          studyFile = file;
        }
        if (file.name === 'releases.xlsx') {
          releasesFile = file;
        }
      });
      if (!studyFile) {
        JobLoggingService.cancel(
          'study-management.log-messages.study.study-file-not-found', {});
        return;
      }
      if (!releasesFile) {
        JobLoggingService.cancel(
          'study-management.log-messages.study.releases-file-not-found', {});
        return;
      }
      ExcelReaderService.readFileAsync(releasesFile)
        .then(function(releases) {
            releasesForStudy = releases;
            return ExcelReaderService.readFileAsync(studyFile);
          }, function() {
          JobLoggingService.cancel(
            'global.log-messages.unsupported-excel-file', {});
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
            function() {
              JobLoggingService.cancel(
                'study-management.log-messages.study.unable-to-delete', {});
            });
        }, function() {
            JobLoggingService.cancel(
              'global.log-messages.unsupported-excel-file', {});
          });
    };

    return {
      uploadStudy: uploadStudy
    };
  });
