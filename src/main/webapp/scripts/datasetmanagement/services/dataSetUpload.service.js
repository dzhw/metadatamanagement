/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('DataSetUploadService',
  function(ExcelReaderService, DataSetBuilderService,
    DataSetDeleteResource, JobLoggingService, $q,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope) {
    var objects;
    var uploadCount;
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
          JobLoggingService.error(
            'data-set-management.log-messages.data-set.missing-id', {
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
              .getErrorMessage(error, 'data-set');
            if (errorMessages.subMessages.length > 0) {
              errorMessages.subMessages.forEach(function(subMessage) {
                if (subMessage.translationParams
                  .indexOf('subDataSets[') !== -1) {
                  var index = parseInt(subMessage
                  .translationParams.replace('subDataSets[', '').split(']')[0]);
                  subMessage.translationParams = {index: index + 1};
                }
              });
              errorMessages.subMessages.sort(function(message1, message2) {
                return message1.translationParams.index -
                  message2.translationParams.index;
              });
            }
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadDataSets = function(files, dataAcquisitionProjectId) {
      uploadCount = 0;
      objects = [];
      var allFileReaders = [];
      JobLoggingService.start('variable');
      DataSetDeleteResource.deleteByDataAcquisitionProjectId({
        dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise.then(
        function() {
          var dataSetExcelFile;
          var subDataSetsExcelFiles = {};

          files.forEach(function(file) {
            if (file.name.endsWith('.xlsx')) {
              if (file.name !== 'dataSets.xlsx') {
                subDataSetsExcelFiles[file.name.replace('.xlsx', '')] = file;
              } else {
                dataSetExcelFile = file;
              }
            }
          });
          if (!dataSetExcelFile) {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'dataSets.xlsx'});
            return;
          }

          ExcelReaderService.readFileAsync(dataSetExcelFile)
          .then(function(dataSets) {
            dataSets.forEach(function(dataSetFromExcel) {
              if (subDataSetsExcelFiles[dataSetFromExcel.id]) {
                allFileReaders.push(ExcelReaderService.
                readFileAsync(subDataSetsExcelFiles[dataSetFromExcel.id]).
                then(function(subDataSetsFile) {
                  var subDataSetErrors = [];
                  var subDataSets = [];
                  for (var i = 0; i < subDataSetsFile.length; i++) {
                    try {
                      subDataSets.push(DataSetBuilderService
                      .buildSubDataSet(subDataSetsFile[i]));
                    }catch (e) {
                      subDataSetErrors = _.concat(subDataSetErrors, e);
                    }
                  }
                  if (subDataSetErrors.length === 0) {
                    objects.push(DataSetBuilderService
                      .buildDataSet(dataSetFromExcel,
                        subDataSets, dataAcquisitionProjectId));
                  } else {
                    JobLoggingService.error(
                      'data-set-management.' +
                      'log-messages.data-set.not-saved',
                      {id: dataSetFromExcel.id},
                      subDataSetErrors);
                    return;
                  }
                }, function() {
                  JobLoggingService
                  .error('global.log-messages.unable-to-read-file',
                    {file: dataSetFromExcel.id + '.xlsx'});
                }));
              } else {
                JobLoggingService.error(
                  'data-set-management.' +
                  'log-messages.data-set.missing-sub-data-set-file',
                  {id: dataSetFromExcel.id});
              }
            });
          }, function() {
            JobLoggingService.cancel('global.log-messages.unable-to-read-file',
              {file: 'dataSets.xlsx'});
            return $q.reject();
          }).then(function() {
              return $q.all(allFileReaders);
            }).then(upload);
        }, function() {
          JobLoggingService.cancel(
            'data-set-management.log-messages.data-set.unable-to-delete');
          return $q.reject();
        }
      );
    };
    return {
      uploadDataSets: uploadDataSets
    };
  });
