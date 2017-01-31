/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('DataSetUploadService',
  function(ExcelReaderService, DataSetBuilderService,
    DataSetDeleteResource, JobLoggingService, $q,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog, CleanJSObjectService) {
    var objects;
    var uploadCount;
    // a map dataSet.number -> true
    var previouslyUploadedDataSetNumbers;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'data-set-management.log-messages.data-set.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
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
                  var dataSets = allExcelSheets.dataSets;
                  var subDataSetsSheet = allExcelSheets.subDataSets;

                  //iterate all subdatasets
                  //the subdata set iteration is the outer loop
                  //Reason: size(subDataSet) >= size(dataSet)
                  subDataSetsSheet.forEach(function(
                    subDataSetFromExcel) {

                    //Variable for Validation: Is the reference of
                    //subDataSet.dataSetNumber valid?
                    var subDataSetNumberFound = false;

                    //iterate the data sets
                    for (var i = 0; i < dataSets.length; i++) {
                      var dataSet = dataSets[i];

                      //Prepare: Adding SubDataSets to the DataSet
                      if (dataSet.subDataSets === undefined) {
                        dataSet.subDataSets = [];
                      }

                      //Prepare: Adding SubDataSets Errors to the DataSet
                      if (dataSet.subDataSetErrors ===
                        undefined) {
                        dataSet.subDataSetErrors = [];
                      }

                      //use only depending sub datasets
                      if (subDataSetFromExcel.dataSetNumber ===
                        dataSet.number) {
                        subDataSetNumberFound = true;

                        //try creating a SubDataSet Object for Mongo
                        try {
                          dataSet.subDataSets.push(
                            DataSetBuilderService
                            .buildSubDataSet(
                              subDataSetFromExcel));
                        } catch (e) {
                          dataSet.subDataSetErrors = _.concat(
                            dataSet.subDataSetErrors, e);
                        }
                      }
                    } //end for

                    //Validation Check
                    //subDataSet.dataSetNumber valid?
                    if (subDataSetNumberFound === false) {
                      JobLoggingService.error({
                        message: 'data-set-management.' +
                          'log-messages.sub-data-set.unknown-data-set-number',
                        messageParams: {
                          name: subDataSetFromExcel.name,
                          dataSetNumber: subDataSetFromExcel
                            .dataSetNumber
                        }
                      });
                    }
                  });

                  //check for errors
                  dataSets.forEach(function(dataSet) {
                    if (dataSet.subDataSetErrors.length === 0) {
                      objects.push(DataSetBuilderService
                        .buildDataSet(dataSet,
                          dataAcquisitionProjectId)
                      );
                    } else {
                      JobLoggingService.error({
                        message: 'data-set-management.' +
                          'log-messages.data-set.not-saved',
                        messageParams: {
                          id: dataSet.id
                        },
                        subMessages: dataSet.subDataSetErrors
                      });
                      return;
                    }
                  });
                }, function() {
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
