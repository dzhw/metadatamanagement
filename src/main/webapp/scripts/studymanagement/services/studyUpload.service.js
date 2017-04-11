/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('StudyUploadService',
  function(StudyBuilderService, StudyDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q,
    ElasticSearchAdminService, $rootScope, $translate, $mdDialog,
    CleanJSObjectService) {
    var study;

    var upload = function() {
      if (!study.id || study.id === '') {
        JobLoggingService.error({
          message: 'study-management.log-messages.study.missing-id',
          messageParams: {
            index: 1
          }
        });
      } else {
        study.$save().then(function() {
          JobLoggingService.success();
        }).catch(function(error) {
          var errorMessages = ErrorMessageResolverService
            .getErrorMessage(error, 'study');
          JobLoggingService.error({
            message: errorMessages.message,
            messageParams: errorMessages.translationParams,
            subMessages: errorMessages.subMessages
          });
        }).then(function() {
          ElasticSearchAdminService.processUpdateQueue('studies').finally(
            function() {
              JobLoggingService.finish(
                'study-management.log-messages.study.upload-terminated', {
                  total: JobLoggingService.getCurrentJob().total,
                  warnings: JobLoggingService.getCurrentJob().warnings,
                  errors: JobLoggingService.getCurrentJob().errors
                });
              $rootScope.$broadcast('upload-completed');
            });
        });
      }
    };

    var uploadStudy = function(files, dataAcquisitionProjectId) {
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        var confirm = $mdDialog.confirm()
          .title($translate.instant(
            'search-management.delete-messages.delete-studies-title'))
          .textContent($translate.instant(
            'search-management.delete-messages.delete-studies', {
              id: dataAcquisitionProjectId
            }))
          .ariaLabel($translate.instant(
            'search-management.delete-messages.delete-studies', {
              id: dataAcquisitionProjectId
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));
        $mdDialog.show(confirm).then(function() {
          JobLoggingService.start('study');
          StudyDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          }).$promise.then(
            function() {
              var studyExcelFile;
              files.forEach(function(file) {
                if (file.name === 'study.xlsx') {
                  studyExcelFile = file;
                }
              });
              if (!studyExcelFile) {
                JobLoggingService.cancel(
                  'study-management.log-messages.study.study-file-not-found', {}
                );
                return;
              }
              ExcelReaderService.readFileAsync(studyExcelFile, true)
                .then(function(allExcelSheets) {
                  var studyFromExcel = allExcelSheets.study;
                  var authorsFromExcel = allExcelSheets.authors;
                  var authors;

                  //Check for the study and authors sheets
                  if (!studyFromExcel || !authorsFromExcel) {
                    JobLoggingService.cancel('global.log-messages.' +
                      'unable-to-read-excel-sheets', {
                        sheets: 'study, authors',
                      });
                    return $q.reqect();
                  }
                  //Get list of authors from excel sheet
                  authors = StudyBuilderService.buildAuthors(
                    authorsFromExcel);

                  //build study
                  study = StudyBuilderService
                    .buildStudy(studyFromExcel[0], authors,
                      dataAcquisitionProjectId);

                  upload();
                }, function() {
                  JobLoggingService.cancel(
                    'global.log-messages.unable-to-read-file', {
                      file: 'study.xlsx'
                    });
                });
            },
            function() {
              JobLoggingService.cancel(
                'study-management.log-messages.study.unable-to-delete'
              );
            }
          );
        });
      }
    };
    return {
      uploadStudy: uploadStudy
    };
  });
