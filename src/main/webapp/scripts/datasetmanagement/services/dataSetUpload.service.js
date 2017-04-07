/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('DataSetUploadService',
  function(ExcelReaderService, DataSetBuilderService,
    DataSetDeleteResource, JobLoggingService, $q,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog, CleanJSObjectService,
    DataSetAttachmentUploadService, DataSetAttachmentBuilderService) {
    var objects;
    var uploadCount;
    var filesMap;
    // a map dataSet.number -> true
    var previouslyUploadedDataSetNumbers;

    var createDataSetFileMap = function(files, dataAcquisitionProjectId) {
      filesMap = {'dataSets': {
        'dataAcquisitionProjectId': dataAcquisitionProjectId,
        'excelFile': '',
        'attachments': {}
      }};
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
            filesMap.dataSets.attachments[file.name] = file;
          break;
          default:
            if (file.name === 'dataSets.xlsx') {
              filesMap.dataSets.excelFile = file;
            }
          break;
        }
      });
    };

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue('data_sets').finally(
          function() {
            JobLoggingService.finish(
              'data-set-management.log-messages.data-set.upload-terminated', {
                total: JobLoggingService.getCurrentJob().total,
                warnings: JobLoggingService.getCurrentJob().warnings,
                errors: JobLoggingService.getCurrentJob().errors
              });
            $rootScope.$broadcast('upload-completed');
          });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error({
            message: 'data-set-management.log-messages.data-set.missing-id',
            messageParams: {
              index: index + 1
            }
          });
          uploadCount++;
          return upload();
        } else if (previouslyUploadedDataSetNumbers[
            objects[uploadCount].number]) {
          JobLoggingService.error({
            message: 'data-set-management.log-messages.data-set.' +
              'duplicate-data-set-number',
            messageParams: {
              index: uploadCount + 1,
              number: objects[uploadCount].number
            }
          });
          uploadCount++;
          return upload();
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success();
            previouslyUploadedDataSetNumbers[objects[uploadCount].number] =
              true;
            uploadCount++;
            return upload();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessage(error, 'data-set');
            if (errorMessages.subMessages.length > 0) {
              errorMessages.subMessages.forEach(function(subMessage) {
                if (subMessage.translationParams.property &&
                  subMessage.translationParams.property
                  .indexOf('subDataSets[') !== -1) {
                  var index = parseInt(subMessage.translationParams
                    .property.replace('subDataSets[', '').split(
                      ']')[0]);
                  subMessage.translationParams = {
                    index: index + 1
                  };
                }
              });
              errorMessages.subMessages.sort(function(message1,
                message2) {
                return message1.translationParams.index -
                  message2.translationParams.index;
              });
            }
            JobLoggingService.error({
              message: errorMessages.message,
              messageParams: errorMessages.translationParams,
              subMessages: errorMessages.subMessages
            });
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadDataSets = function(files, dataAcquisitionProjectId) {
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        var confirm = $mdDialog.confirm()
          .title($translate.instant(
            'search-management.delete-messages.' +
            'delete-data-sets-title'))
          .textContent($translate.instant(
            'search-management.delete-messages.delete-data-sets', {
              id: dataAcquisitionProjectId
            }))
          .ariaLabel($translate.instant(
            'search-management.delete-messages.delete-data-sets', {
              id: dataAcquisitionProjectId
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));
        $mdDialog.show(confirm).then(function() {
          uploadCount = 0;
          objects = [];
          previouslyUploadedDataSetNumbers = {};
          var allFileReaders = [];
          JobLoggingService.start('variable');
          DataSetDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId
          }).$promise.then(
            function() {

              createDataSetFileMap(files, dataAcquisitionProjectId);

              var dataSetExcelFile;
              var subDataSetsExcelFiles = {};

              files.forEach(function(file) {
                if (file.name.endsWith('.xlsx')) {
                  if (file.name !== 'dataSets.xlsx') {
                    subDataSetsExcelFiles[file
                      .name.replace('.xlsx', '')] = file;
                  } else {
                    dataSetExcelFile = file;
                  }
                }
              });
              if (!dataSetExcelFile) {
                JobLoggingService.cancel('global.log-messages.' +
                  'unable-to-read-file', {
                    file: 'dataSets.xlsx'
                  });
                return;
              }

              //Read excel file with DataSets and SubDataSets
              ExcelReaderService.readFileAsync(dataSetExcelFile, true)
                .then(function(allExcelSheets) {
                    var dataSetsSheet = allExcelSheets.dataSets;
                    var subDataSetsSheet = allExcelSheets.subDataSets;
                    var attachmentsSheet = allExcelSheets.attachments;
                    var dataSetMap = {};
                    var subDataSetMap = {};
                    var subDataSetErrors = {};

                    if (!dataSetsSheet || !subDataSetsSheet) {
                      JobLoggingService.cancel('global.log-messages.' +
                        'unable-to-read-excel-sheets', {
                          sheets: 'dataSets, subDataSets'
                        });
                      return $q.reqect();
                    }

                    //Build a dataset Map
                    dataSetsSheet.forEach(function(dataSetFromExcel) {
                      dataSetMap[dataSetFromExcel.number] =
                        dataSetFromExcel;
                    });

                    //Build a subDataSetMap
                    subDataSetsSheet.forEach(function(
                      subDataSetFromExcel) {

                      //Validate the SubDataSet.dataSetNumber is valid
                      if (dataSetMap[subDataSetFromExcel.dataSetNumber] !==
                        undefined) { //Valid Case

                        //Check for an empty array for sub data sets
                        if (subDataSetMap[subDataSetFromExcel
                            .dataSetNumber] === undefined) {
                          subDataSetMap[subDataSetFromExcel.dataSetNumber] = [];
                        }

                        //check for an empty array for errors of sub data sets
                        if (subDataSetErrors[
                            subDataSetFromExcel.dataSetNumber] ===
                          undefined) {
                          subDataSetErrors[subDataSetFromExcel
                            .dataSetNumber] = [];
                        }

                        //Build Sub Data Sets
                        try {
                          subDataSetMap[subDataSetFromExcel.dataSetNumber]
                            .push(DataSetBuilderService
                              .buildSubDataSet(
                                subDataSetFromExcel)
                            );
                        } catch (e) {
                          subDataSetErrors[subDataSetFromExcel.dataSetNumber] =
                            _.concat(subDataSetErrors[
                              subDataSetFromExcel.dataSetNumber
                            ], e);
                        }
                      } else { //Not valid SubDataSet.dataSetNumber Case
                        JobLoggingService.warning({
                          message: 'data-set-management.' +
                            'log-messages.sub-data-set.unknown-data-set-number',
                          messageParams: {
                            index: (subDataSetFromExcel.__rowNum__ +
                              1),
                            dataSetNumber: subDataSetFromExcel
                              .dataSetNumber
                          }
                        });
                      }
                    });

                    //TODO here
                    var attachmentUploadObjects =
                      DataSetAttachmentBuilderService
                        .buildDataSetAttachment(attachmentsSheet,
                        dataAcquisitionProjectId, filesMap);
                    var asyncFilesUpload = $q.when();
                    attachmentUploadObjects
                    .forEach(function(attachmentUploadObj) {
                      asyncFilesUpload = asyncFilesUpload
                        .then(function() {
                          return DataSetAttachmentUploadService
                            .uploadAttachment(attachmentUploadObj.file,
                              attachmentUploadObj.metadata);
                        }).then(function() {
                          JobLoggingService.success({
                            objectType: 'attachment'
                          });
                        }).catch(function(error) {
                          // attachment upload failed
                          var errorMessage =
                            ErrorMessageResolverService
                            .getErrorMessage(error, 'dataSet',
                            'dataSet-attachment',
                            attachmentUploadObj.file.name);
                          JobLoggingService.error({
                            message: errorMessage.message,
                            messageParams: errorMessage.translationParams,
                            subMessages: errorMessage.subMessages,
                            objectType: 'attachment'
                          });
                        });
                    });

                    //Build all DataSets
                    dataSetsSheet.forEach(function(
                      dataSetFromExcel) {

                      //Add SubDataSets to the DataSet
                      var dataSet = dataSetMap[dataSetFromExcel.number];
                      dataSet.subDataSets = subDataSetMap[
                        dataSetFromExcel.number];

                      //Build DataSet
                      if (subDataSetErrors[dataSet.number].length ===
                        0) {
                        objects.push(DataSetBuilderService
                          .buildDataSet(dataSet,
                            dataAcquisitionProjectId)
                        );
                      } else { //No Upload -> Error!
                        JobLoggingService.error({
                          message: 'data-set-management.' +
                            'log-messages.data-set.not-saved',
                          messageParams: {
                            id: dataSet.id
                          },
                          subMessages: subDataSetErrors[
                            dataSet.number]
                        });
                        return;
                      }
                    });
                  },
                  function() {
                    JobLoggingService.cancel('global.log-messages.' +
                      'unable-to-read-file', {
                        file: 'dataSets.xlsx'
                      });
                    return $q.reject();
                  }).then(function() {
                  return $q.all(allFileReaders);
                }).then(upload);
            },
            function() {
              JobLoggingService.cancel(
                'data-set-management.log-messages.data-set.unable-to-delete'
              );
              return $q.reject();
            }
          );
        });
      }
    };
    return {
      uploadDataSets: uploadDataSets
    };
  });
