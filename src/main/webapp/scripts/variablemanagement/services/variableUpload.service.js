/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
  function(VariableBuilderService, VariableDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q, FileReaderService,
    ElasticSearchAdminService, $rootScope, $translate, $mdDialog,
    CleanJSObjectService) {
    var objects;
    var uploadCount;
    var filesMap;
    var toUploadDataSet;

    // should be changed, its a workaround
    var defer = $q.defer();
    var upload = function() {
      if (uploadCount === objects.length) {
        defer.resolve();
      } else {
        if (objects[uploadCount] === null) {
          uploadCount++;
          return upload();
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success();
            uploadCount++;
            return upload();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessage(error, 'variable');
            JobLoggingService.error({message: errorMessages.message,
              messageParams: errorMessages.translationParams,
              subMessages: errorMessages.subMessages
            });
            uploadCount++;
            return upload();
          });
        }
      }
      return defer.promise;
    };
    var createDataSetsFileMap = function(files, dataAcquisitionProjectId) {
      filesMap = {};
      var dataSetIndex = 0;
      files.forEach(function(file) {
        var path;
        if (file.path) {
          path = _.split(file.path, '/');
        } else {
          if (file.webkitRelativePath) {
            path = _.split(file.webkitRelativePath, '/');
          }
        }
        var pathLength = path.length;
        if (file.name === 'variables.xlsx') {
          if (!filesMap[path[pathLength - 2]]) {
            filesMap[path[pathLength - 2]] = {};
            filesMap[path[pathLength - 2]].dataAcquisitionProjectId =
            dataAcquisitionProjectId;
            filesMap[path[pathLength - 2]].dataSet =
            path[pathLength - 2];
            filesMap[path[pathLength - 2]].dataSetIndex = dataSetIndex;
            filesMap[path[pathLength - 2]].jsonFiles = {};
            dataSetIndex++;
          }
          filesMap[path[pathLength - 2]].excelFile = file;
        }
        if (file.name.endsWith('.json')) {
          var variableName = _.split(file.name, '.json')[0];
          if (!filesMap[path[pathLength - 3]]) {
            filesMap[path[pathLength - 3]] = {};
            filesMap[path[pathLength - 3]].dataAcquisitionProjectId =
            dataAcquisitionProjectId;
            filesMap[path[pathLength - 3]].dataSet =
            path[pathLength - 3];
            filesMap[path[pathLength - 3]].dataSetIndex = dataSetIndex;
            filesMap[path[pathLength - 3]].jsonFiles = {};
            dataSetIndex++;
          }
          filesMap[path[pathLength - 3]].jsonFiles[variableName] = file;
        }
      });
      return filesMap;
    };
    var createVariableObjects = function(variables, files) {
      toUploadDataSet = files.dataSet;
      variables.forEach(function(variableFromExcel, variableIndex) {
          if (variableFromExcel.name) {
            if (files.jsonFiles[variableFromExcel.name]) {
              FileReaderService.readAsText(files
              .jsonFiles[variableFromExcel.name])
              .then(function(variableAsText) {
                try {
                  var variableFromJson = JSON.parse(variableAsText);
                  objects.push(VariableBuilderService
                    .buildVariable(variableFromExcel, variableFromJson,
                      files.dataAcquisitionProjectId, files.dataSet));
                  if (variableIndex === (variables.length - 1)) {
                    defer.resolve();
                  }
                } catch (e) {
                  JobLoggingService.error({
                    message: 'variable-management.log-messages.' +
                    'variable.json-parse-error',
                    messageParams: {
                      dataSet: files.dataSet,
                      file: variableFromExcel.name + '.json'}
                  });
                  if (variableIndex === (variables.length - 1)) {
                    defer.resolve();
                  }
                }}, function() {
                    JobLoggingService.error({
                      message: 'variable-management.log-messages.variable' +
                      '.unable-to-read-file',
                      messageParams: {
                        dataSet: files.dataSet,
                        file: variableFromExcel.name + '.json'
                      }
                    });
                  });
            } else {
              JobLoggingService.error({
                message: 'variable-management.log-messages.variable' +
                '.missing-json-file',
                messageParams: {
                  dataSet: files.dataSet,
                  name: variableFromExcel.name
                }
              });
            }
          } else {
            JobLoggingService.error({
              message: 'variable-management.log-messages.variable.missing-name',
              messageParams: {
                dataSet: files.dataSet,
                variableIndex: variableIndex + 1
              }
            });
          }
        });
      return defer.promise;
    };

    var uploadDataSets = function(dataSetIndex) {
      if (dataSetIndex === (_.size(filesMap))) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'variable-management.log-messages.variable.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
        return;
      } else {
        var dataSet = _.filter(filesMap, function(o) {
          return o.dataSetIndex === dataSetIndex ;
        })[0];
        if (!dataSet.excelFile) {
          JobLoggingService.error({
            message:
            'variable-management.log-messages.variable.missing-excel-file',
            messageParams: {
              dataSet: dataSet.dataSet
            }
          });
          dataSetIndex++;
          return uploadDataSets(dataSetIndex);
        }
        ExcelReaderService.readFileAsync(dataSet.excelFile)
        .then(function(variables) {
            createVariableObjects(variables, dataSet).then(function() {
                  defer = $q.defer();
                  uploadCount = 0;
                  upload().then(function() {
                    dataSetIndex++;
                    uploadDataSets(dataSetIndex);
                  });
                });
          }, function() {
            JobLoggingService.error({
              message: 'variable-management.log-messages.variable.' +
              'unable-to-read-file',
              messageParams: {
                dataSet: dataSet.dataSet,
                file: 'variables.xlsx'
              }
            });
            dataSetIndex++;
            uploadDataSets(dataSetIndex);
          });
      }
    };

    var uploadVariables = function(files, dataAcquisitionProjectId) {
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        var confirm = $mdDialog.confirm()
          .title($translate.instant(
            'search-management.delete-messages.' +
            'delete-variables-title'))
          .textContent($translate.instant(
            'search-management.delete-messages.delete-variables', {
              id: dataAcquisitionProjectId
            }))
          .ariaLabel($translate.instant(
            'search-management.delete-messages.delete-variables', {
              id: dataAcquisitionProjectId
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));
        $mdDialog.show(confirm).then(function() {
          JobLoggingService.start('variable');
          VariableDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise.then(
            function() {
              objects = [];
              createDataSetsFileMap(files, dataAcquisitionProjectId);
              uploadDataSets(0);
            }, function() {
              JobLoggingService.cancel(
                'variable-management.log-messages.variable.unable-to-delete');
            }
          );
        });
      }
    };

    return {
      uploadVariables: uploadVariables
    };
  });
