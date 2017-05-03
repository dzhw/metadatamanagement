/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('StudyUploadService',
  function(StudyBuilderService, StudyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $translate, $mdDialog,
    StudyAttachmentUploadService, $q, ElasticSearchAdminService, $rootScope) {
      var createStudyFilesMap = function(files, dataAcquisitionProjectId) {
        var studyFilesMap = {
          dataAcquisitionProjectId: dataAcquisitionProjectId,
          attachments: {}
        };
        files.forEach(function(file) {
          var path;
          if (file.path) {
            path = _.split(file.path, '/');
          } else {
            if (file.webkitRelativePath) {
              path = _.split(file.webkitRelativePath, '/');
            } else {
              path = [file.name];
            }
          }
          var parentFolder = _.last(_.initial(path));
          switch (parentFolder) {
            case 'attachments':
              studyFilesMap.attachments[file.name] = file;
            break;
            default:
              if (file.name === 'study.xlsx') {
                studyFilesMap['excelFile'] = file; /* jshint ignore:line */
              }
            break;
          }
        });
        return studyFilesMap;
      };
      var finishUpload = function() {
        ElasticSearchAdminService.processUpdateQueue('studies').finally(
          function() {
            JobLoggingService.finish(
              'study-management.log-messages.study.upload-terminated', {
                total: JobLoggingService
                  .getCurrentJob().getCounts('study').total,
                attachments: JobLoggingService
                  .getCurrentJob().getCounts('attachment').total,
                warnings: JobLoggingService.getCurrentJob().warnings,
                errors: JobLoggingService.getCurrentJob().errors
              });
            $rootScope.$broadcast('upload-completed');
          });
      };
      var uploadAttachmentAsync = function(attachments) {
            var chainedAttachmentUploads = $q.when();
            attachments.forEach(function(attachmentUploadObj) {
              chainedAttachmentUploads = chainedAttachmentUploads.then(
                function() {
                  return StudyAttachmentUploadService
                  .uploadAttachment(attachmentUploadObj.file,
                    attachmentUploadObj.metaData).then(function()  {
                      JobLoggingService.success({
                        objectType: 'attachment'
                      });
                    }).catch(function(error) {
                      var errorMessage = ErrorMessageResolverService
                      .getErrorMessage(error, 'study', 'study-attachment',
                      attachmentUploadObj.file.name);
                      JobLoggingService.error({
                        message: errorMessage.message,
                        messageParams: errorMessage.translationParams,
                        subMessages: errorMessage.subMessages,
                        objectType: 'attachment'
                      });
                    });
                });
            });
            chainedAttachmentUploads.finally(function() {
              finishUpload();
            });

          };
      var upload = function(toBeUploadedStudy) {
        toBeUploadedStudy.study.$save().then(function() {
          JobLoggingService.success({
            objectType: 'study'
          });
          uploadAttachmentAsync(toBeUploadedStudy.attachments);
        }).catch(function(error) {
          var errorMessages = ErrorMessageResolverService
            .getErrorMessage(error, 'study');
          JobLoggingService.error({
            message: errorMessages.message,
            messageParams: errorMessages.translationParams,
            subMessages: errorMessages.subMessages
          });
          finishUpload();
        });
      };
      var createReadyToUploadStudy = function(studyResource, attachmentFiles) {
        var readyToUploadStudy = {
          study: studyResource.study,
          attachments: []
        };
        studyResource.attachments.forEach(function(attachment) {
          if (attachmentFiles[attachment.fileName]) {
            readyToUploadStudy.attachments.push({
              file: attachmentFiles[attachment.fileName],
              metaData: attachment
            });
          } else {
            JobLoggingService.error({
              message: 'study-management.log-messages' +
              '.study-attachment.file-not-found',
              messageParams: {
                filename: attachment.fileName
              },
              objectType: 'attachment'
            });
          }
        });
        return readyToUploadStudy;
      };
      var createStudyResource = function(studyFilesMap) {
        return $q(function(resolve) {
          if (studyFilesMap.excelFile) {
            ExcelReaderService.readFileAsync(studyFilesMap.excelFile, true)
              .then(function(allExcelSheets) {
                var studyFromExcel = allExcelSheets.study;
                var authorsFromExcel = allExcelSheets.authors;
                var attachmentsFromExcel = allExcelSheets.attachments;
                if (!studyFromExcel || !authorsFromExcel ||
                  !attachmentsFromExcel) {
                  JobLoggingService.cancel('global.log-messages.' +
                    'unable-to-read-excel-sheets', {
                      sheets: 'study, authors, attachment',
                    });
                  resolve();
                }
                var authors = StudyBuilderService.buildAuthors(
                  authorsFromExcel);
                var study = StudyBuilderService.buildStudy(studyFromExcel[0],
                  authors, studyFilesMap.dataAcquisitionProjectId);
                var attachments = StudyBuilderService.
                buildStudyAttachmentsMetadata(attachmentsFromExcel,
                  studyFilesMap.dataAcquisitionProjectId, study.id);
                var studyResource = {
                  study: study,
                  attachments: attachments
                };
                resolve(studyResource);
              }, function() {
                JobLoggingService.cancel(
                  'global.log-messages.unable-to-read-file', {
                    file: 'study.xlsx'
                  });
              });
          } else {
            JobLoggingService.cancel(
              'study-management.log-messages.study.study-file-not-found', {}
            );
            resolve();
          }
        });
      };
      var startJob = function(files, dataAcquisitionProjectId) {
        JobLoggingService.start('study');
        StudyDeleteResource.deleteByDataAcquisitionProjectId({
          dataAcquisitionProjectId: dataAcquisitionProjectId
        }).$promise.then(function() {
          var filesMap = createStudyFilesMap(files,
            dataAcquisitionProjectId);
          createStudyResource(filesMap).then(function(studyResource) {
              var toBeUploadedStudy = createReadyToUploadStudy(studyResource,
              filesMap.attachments);
              upload(toBeUploadedStudy);
            });
        }, function() {
            JobLoggingService.cancel(
              'study-management.log-messages.study.unable-to-delete'
            );
          });
      };
      var uploadStudy = function(files, dataAcquisitionProjectId) {
        var confirm = $mdDialog.confirm().title($translate.instant(
          'search-management.delete-messages.delete-studies-title'))
          .textContent($translate.instant(
            'search-management.delete-messages.delete-studies', {
              id: dataAcquisitionProjectId
            })).ariaLabel($translate.instant(
              'search-management.delete-messages.delete-studies', {
                id: dataAcquisitionProjectId
              })).ok($translate.instant('global.buttons.ok'))
              .cancel($translate.instant('global.buttons.cancel'));
        $mdDialog.show(confirm).then(function() {
            startJob(files, dataAcquisitionProjectId);
          });
      };
      return {
        uploadStudy: uploadStudy
      };
    });
