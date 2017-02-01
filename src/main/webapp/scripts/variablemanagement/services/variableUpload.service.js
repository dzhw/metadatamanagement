/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
  function(VariableBuilderService, VariableDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q, FileReaderService,
    ElasticSearchAdminService, $rootScope, $translate, $mdDialog,
    CleanJSObjectService) {
    var filesMap;
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
            filesMap[path[pathLength - 2]].dataSetName =
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
            filesMap[path[pathLength - 3]].dataSetName =
            path[pathLength - 3];
            filesMap[path[pathLength - 3]].dataSetIndex = dataSetIndex;
            filesMap[path[pathLength - 3]].jsonFiles = {};
            dataSetIndex++;
          }
          filesMap[path[pathLength - 3]].jsonFiles[variableName] = file;
        }
      });
    };
    var createVariableObjects = function(dataSet) {
      var variablesResources = [];
      return $q(function(resolve) {
        ExcelReaderService.readFileAsync(dataSet.excelFile)
      .then(function(variables) {
          variables.forEach(function(variableFromExcel, variableIndex) {
            if (variableFromExcel.name) {
              if (dataSet.jsonFiles[variableFromExcel.name]) {
                FileReaderService.readAsText(dataSet
                .jsonFiles[variableFromExcel.name])
                .then(function(variableAsText) {
                  try {
                    var variableFromJson = JSON.parse(variableAsText);
                    variablesResources.push(VariableBuilderService
                      .buildVariable(variableFromExcel, variableFromJson,
                        dataSet.dataAcquisitionProjectId, dataSet.dataSetName));
                    if (variableIndex === (variables.length - 1)) {
                      resolve(variablesResources);
                    }
                  } catch (e) {
                    JobLoggingService.error({
                      message: 'variable-management.log-messages.' +
                      'variable.json-parse-error',
                      messageParams: {
                        dataSet: dataSet.dataSetName,
                        file: variableFromExcel.name + '.json'}
                    });
                    if (variableIndex === (variables.length - 1)) {
                      resolve(variablesResources);
                    }
                  }}, function() {
                      JobLoggingService.error({
                        message: 'variable-management.log-messages.variable' +
                        '.unable-to-read-file',
                        messageParams: {
                          dataSet: dataSet.dataSetName,
                          file: variableFromExcel.name + '.json'
                        }
                      });
                      if (variableIndex === (variables.length - 1)) {
                        resolve(variablesResources);
                      }
                    });
              } else {
                JobLoggingService.error({
                  message: 'variable-management.log-messages.variable' +
                  '.missing-json-file',
                  messageParams: {
                    dataSet: dataSet.dataSetName,
                    name: variableFromExcel.name
                  }
                });
              }
            } else {
              JobLoggingService.error({
                message: 'variable-management.log-messages' +
                '.variable.missing-name',
                messageParams: {
                  dataSet: dataSet.dataSetName,
                  variableIndex: variableIndex + 1
                }
              });
              if (variableIndex === (variables.length - 1)) {
                resolve(variablesResources);
              }
            }
          });
        }, function() {
          JobLoggingService.error({
            message: 'variable-management.log-messages.variable.' +
            'unable-to-read-file',
            messageParams: {
              dataSet: dataSet.dataSetName,
              file: 'variables.xlsx'
            }
          });
          resolve(variablesResources);
        });
      });
    };
    var uploadVariable = function(variable) {
      return $q(function(resolve) {
          variable.$save().then(function() {
            JobLoggingService.success();
            resolve();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessage(error, 'variable');
            JobLoggingService.error({message: errorMessages.message,
              messageParams: errorMessages.translationParams,
              subMessages: errorMessages.subMessages
            });
            resolve();
          });
        });
    };
    var uploadDataSets = function(dataSetIndex) {
      if (dataSetIndex === _.size(filesMap)) {
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
        var dataSet = _.filter(filesMap, function(filesObject) {
          return filesObject.dataSetIndex === dataSetIndex ;
        })[0];
        if (!dataSet.excelFile) {
          JobLoggingService.error({
            message:
            'variable-management.log-messages.variable.missing-excel-file',
            messageParams: {
              dataSet: dataSet.dataSetName
            }
          });
          dataSetIndex++;
          return uploadDataSets(dataSetIndex);
        }
        createVariableObjects(dataSet).then(function(variables) {
          var promises = [];
          variables.forEach(function(variable) {
            promises.push(uploadVariable(variable));
          });
          $q.all(promises).then(function() {
            dataSetIndex++;
            uploadDataSets(dataSetIndex);
          });
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
