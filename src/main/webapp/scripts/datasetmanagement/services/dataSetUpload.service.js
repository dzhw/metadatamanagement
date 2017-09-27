/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('DataSetUploadService',
  function(ExcelReaderService, DataSetBuilderService,
    DataSetRepositoryClient, JobLoggingService, $q, DataSetResource,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog,
    DataSetAttachmentUploadService, DataSetIdBuilderService) {
    var objects;
    var attachmentMap;
    var uploadCount;
    var filesMap;
    // a map dataSet.number -> true
    var previouslyUploadedDataSetNumbers;
    // map dataSetId -> presentInExcel true/false
    var existingDataSets = {};
    var createDataSetFileMap = function(files, dataAcquisitionProjectId) {
      filesMap = {
        'dataSets': {
          'dataAcquisitionProjectId': dataAcquisitionProjectId,
          'excelFile': '',
          'attachments': {}
        }
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

    var deleteAttachments = function(dataSetId) {
      var deferred = $q.defer();
      DataSetAttachmentUploadService.deleteAllAttachments(dataSetId)
      .catch(function(error) {
        console.log('Unable to delete attachments:' + error);
      }).finally(function() {
        deferred.resolve();
      });
      return deferred.promise;
    };

    var deleteAllDataSetsNotPresentInExcel = function() {
      var promiseChain = $q.when();
      _.each(existingDataSets, function(existingDataSet) {
        if (!existingDataSet.presentInExcel) {
          promiseChain = promiseChain.then(function() {
            return DataSetResource.delete({id: existingDataSet.id}).$promise
            .catch(
              function(error) {
                console.log('Error when deleting dataSet:', error);
              });
          });
        }
      });
      return promiseChain;
    };

    var upload = function() {
      //the uploads are finished
      if (uploadCount === objects.length) {
        deleteAllDataSetsNotPresentInExcel().finally(function() {
          ElasticSearchAdminService.processUpdateQueue('data_sets').finally(
            function() {
              JobLoggingService.finish(
                'data-set-management.log-messages.data-set.upload-terminated', {
                  total: JobLoggingService
                  .getCurrentJob().getCounts('dataSet').total,
                  attachments: JobLoggingService
                  .getCurrentJob().getCounts('attachment').total,
                  warnings: JobLoggingService.getCurrentJob().warnings,
                  errors: JobLoggingService.getCurrentJob().errors
                });
              $rootScope.$broadcast('upload-completed');
            });
        });
        //Further uploads
      } else {
        //Check for missing id
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error({
            message: 'data-set-management.log-messages.data-set.missing-id',
            messageParams: {
              //+1 for index starts with 0, +1 for the headline in excel doc
              index: index + 2
            }
          });
          uploadCount++;
          return upload();
          //check for double used dataset id
        } else if (previouslyUploadedDataSetNumbers[
            objects[uploadCount].number]) {
          JobLoggingService.error({
            message: 'data-set-management.log-messages.data-set.' +
              'duplicate-data-set-number',
            messageParams: {
              //+1 for index = 0, +1 for headline in excel
              index: uploadCount + 2,
              number: objects[uploadCount].number
            }
          });
          uploadCount++;
          return upload();
          //Everything is ok for a regular upload
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success({
              objectType: 'dataSet'
            });
            previouslyUploadedDataSetNumbers[objects[uploadCount].number] =
              true;
            deleteAttachments(objects[uploadCount].id).then(function() {
              //Get all Attachment of a Data Set Number
              if (attachmentMap[objects[uploadCount].number] !==
                undefined) {
                var attachments = attachmentMap[objects[uploadCount].number];
                var chainedAttachmentUploads = $q.when();
                attachments.forEach(function(attachmentUploadObj) {
                      chainedAttachmentUploads = chainedAttachmentUploads.then(
                        function() {
                          return DataSetAttachmentUploadService
                          .uploadAttachment(attachmentUploadObj.file,
                            attachmentUploadObj.metadata).then(function()  {
                              JobLoggingService.success({
                                objectType: 'attachment'
                              });
                            }).catch(function(error) {
                              // attachment upload failed
                              var errorMessage =
                              ErrorMessageResolverService
                              .getErrorMessage(error, 'data-set',
                              'data-set-attachment',
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
                  uploadCount++;
                  return upload();
                });
                //No attachment. Next DataSet
              } else {
                uploadCount++;
                return upload();
              }
            });
            // validation error handling for dataSet
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessage(error, 'data-set');
            var subMessage;
            if (errorMessages.subMessages.length > 0) {
              for (var i = 0; i < errorMessages.subMessages.length; ++i) {
                subMessage = errorMessages.subMessages[i];
                //+2, one line, because it starts at zero
                //the second addiional line is because of the
                //headline in the excel
                subMessage.translationParams.index = uploadCount + 2;
                if (subMessage.translationParams.property &&
                  subMessage.translationParams.property
                  .indexOf('subDataSets[') !== -1) {
                  var index = parseInt(subMessage.translationParams
                    .property.replace('subDataSets[', '').split(
                      ']')[0]);
                  subMessage.translationParams = {
                    index: index + 2
                  };
                }
              }
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

    var startJob = function(files, dataAcquisitionProjectId) {
      uploadCount = 0;
      objects = [];
      attachmentMap = {};
      previouslyUploadedDataSetNumbers = {};
      var allFileReaders = [];
      JobLoggingService.start('variable');

      createDataSetFileMap(files, dataAcquisitionProjectId);

      var dataSetExcelFile;

      files.forEach(function(file) {
        if (file.name.endsWith('.xlsx')) {
          if (file.name === 'dataSets.xlsx') {
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

            if (!dataSetsSheet || !subDataSetsSheet || !
              attachmentsSheet) {
              JobLoggingService.cancel('global.log-messages.' +
                'unable-to-read-excel-sheets', {
                  sheets: 'dataSets, subDataSets, attachments',
                });
              return $q.reqect();
            }

            //Build a dataset Map
            dataSetsSheet.forEach(function(dataSetFromExcel) {
              var dataSetId = DataSetIdBuilderService.buildDataSetId(
                dataAcquisitionProjectId, dataSetFromExcel.number
              );
              dataSetMap[dataSetFromExcel.number] =
                dataSetFromExcel;
              if (existingDataSets[dataSetId]) {
                existingDataSets[dataSetId].presentInExcel = true;
              }
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

            //Prepare Attachments for Upload
            attachmentsSheet.forEach(function(attachment) {
              if (dataSetMap[attachment.dataSetNumber] !==
                undefined) {

                //Check for an empty array for attachments
                if (attachmentMap[attachment
                    .dataSetNumber] === undefined) {
                  attachmentMap[attachment.dataSetNumber] = [];
                }

                //Add Attachment to a list for
                //the depending dataset number
                attachmentMap[attachment.dataSetNumber]
                  .push(DataSetBuilderService
                    .buildDataSetAttachment(attachment,
                      dataAcquisitionProjectId, filesMap,
                      attachment.__rowNum__)
                  );
              } else {
                //Not valid Attachment.dataSetNumber Case
                JobLoggingService.warning({
                  message: 'data-set-management.log-messages.' +
                    'data-set-attachment.unknown-data-set-number',
                  messageParams: {
                    index: (attachment.__rowNum__ + 1),
                    dataSetNumber: attachment.dataSetNumber
                  }
                });
              }
            });

            //Build all DataSets
            dataSetsSheet.forEach(function(
              dataSetFromExcel) {

              //Add SubDataSets to the DataSet
              var dataSet = dataSetMap[dataSetFromExcel.number];
              dataSet.subDataSets = subDataSetMap[
                dataSetFromExcel.number];

              //Build DataSet
              if (!subDataSetErrors[dataSet.number] ||
                subDataSetErrors[dataSet.number].length === 0) {
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
    };

    var uploadDataSets = function(files, dataAcquisitionProjectId) {
      existingDataSets = {};
      DataSetRepositoryClient.findByDataAcquisitionProjectId(
        dataAcquisitionProjectId).then(function(result) {
          result.data.forEach(function(dataSet) {
            existingDataSets[dataSet.id] = dataSet;
          });
          if (result.data.length > 0) {
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
                startJob(files, dataAcquisitionProjectId);
              });
          } else {
            startJob(files, dataAcquisitionProjectId);
          }
        });
    };
    return {
      uploadDataSets: uploadDataSets
    };
  });
