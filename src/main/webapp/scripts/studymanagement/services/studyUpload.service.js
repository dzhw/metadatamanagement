/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('StudyUploadService',
  function(StudyBuilderService, StudyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService, $rootScope) {
    var study;

    var upload = function() {
      if (!study.id || study.id === '') {
        JobLoggingService.error(
          'study-management.log-messages.study.missing-id', {
            index: 1
          });
      } else {
        study.$save().then(function() {
            JobLoggingService.success();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'study');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
          }).then(function() {
            ElasticSearchAdminService.processUpdateQueue().finally(function() {
              JobLoggingService.finish(
                'study-management.log-messages.study.upload-terminated', {
                  total: JobLoggingService.getCurrentJob().total,
                  errors: JobLoggingService.getCurrentJob().errors
                });
              $rootScope.$broadcast('upload-completed');
            });
          });
      }
    };

    var uploadStudy = function(files, dataAcquisitionProjectId) {
      var allFileReaders = [];
      JobLoggingService.start('study');
      StudyDeleteResource.deleteByDataAcquisitionProjectId({
        dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise.then(
        function() {
          var studyExcelFile;
          var releasesExcelFile;
          files.forEach(function(file) {
            if (file.name === 'releases.xlsx') {
              releasesExcelFile = file;
            } else {
              if (file.name === 'study.xlsx') {
                studyExcelFile = file;
              }
            }
          });
          if (!studyExcelFile) {
            JobLoggingService.cancel(
              'study-management.log-messages.study.study-file-not-found', {});
            return;
          }
          if (!releasesExcelFile) {
            JobLoggingService.cancel(
              'study-management.log-messages.study.releases-file-not-found',
              {});
            return;
          }
          ExcelReaderService.readFileAsync(releasesExcelFile)
          .then(function(releasesFromExcel) {
            var releases = StudyBuilderService.buildReleases(releasesFromExcel);
            allFileReaders.push(ExcelReaderService.readFileAsync(studyExcelFile)
            .then(function(studyFromExcel) {
              study = StudyBuilderService
              .buildStudy(studyFromExcel[0], releases,
              dataAcquisitionProjectId);
            }, function() {
              JobLoggingService.cancel(
                'global.log-messages.unable-to-read-file',
                {file: 'study.xlsx'});
              return $q.reject();
            }));
          }, function() {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'releases.xlsx'});
            return $q.reject();
          }).then(function() {
              return $q.all(allFileReaders);
            }).then(upload);
        }, function() {
          JobLoggingService.cancel(
            'study-management.log-messages.study.unable-to-delete');
          return $q.reject();
        }
      );
    };
    return {
      uploadStudy: uploadStudy
    };
  });
