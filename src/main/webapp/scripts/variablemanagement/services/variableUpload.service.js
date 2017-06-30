/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
  function(VariableBuilderService, VariableRepositoryClient,
    JobLoggingService,
    ErrorMessageResolverService, ExcelReaderService, $q, FileReaderService,
    ElasticSearchAdminService, $rootScope, $translate, $mdDialog,
    CleanJSObjectService, VariableResource) {
    var filesMap;
    var existingVariables = {}; // map variableId -> providedByUser true/false

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
            filesMap[path[pathLength - 2]].dataSetNumber =
              _.split(path[pathLength - 2], 'ds')[1];
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
            filesMap[path[pathLength - 3]].dataSetNumber =
              _.split(path[pathLength - 3], 'ds')[1];
            filesMap[path[pathLength - 3]].dataSetIndex = dataSetIndex;
            filesMap[path[pathLength - 3]].jsonFiles = {};
            dataSetIndex++;
          }
          filesMap[path[pathLength - 3]].jsonFiles[variableName] = file;
        }
      });
    };

    var checkForHistogramOnRatioVariables = function(variableFromExcel,
      variableFromJson, dataSetName) {
      if (variableFromJson.scaleLevel != null &&
        variableFromJson.scaleLevel.en === 'ratio' &&
        variableFromJson.scaleLevel.de === 'verhältnis') {
        if (variableFromJson.distribution == null ||
          variableFromJson.distribution.histogram == null ||
          variableFromJson.distribution.histogram.numberOfBins == null ||
          variableFromJson.distribution.histogram.start == null ||
          variableFromJson.distribution.histogram.end == null) {
          JobLoggingService.warning({
            message: 'variable-management.' +
              'log-messages.variable.distribution.histogram.' +
              'incomplete-histogram-information',
            messageParams: {
              variableName: variableFromExcel.name,
              dataSetName: dataSetName
            }
          });
        }
      }
    };

    var createJsonFileReader = function(dataSet, variableFromExcel,
      variablesResources, variableIndex) {
      return FileReaderService.readAsText(dataSet
          .jsonFiles[variableFromExcel.name])
        .then(function(variableAsText) {
          try {
            var variableFromJson = JSON.parse(
              variableAsText);

            //Check for Histogram at ratio variables
            checkForHistogramOnRatioVariables(variableFromExcel,
              variableFromJson, dataSet.dataSetName);

            var variableResource = VariableBuilderService
              .buildVariable(variableFromExcel,
                variableFromJson, dataSet, variableIndex);
            variablesResources.push(variableResource);
            if (existingVariables[variableResource.id]) {
              existingVariables[variableResource.id]
                .providedByUser = true;
            }
          } catch (e) {
            console.log(e);
            JobLoggingService.error({
              message: 'variable-management.log-messages.' +
                'variable.json-parse-error',
              messageParams: {
                dataSet: dataSet.dataSetName,
                file: variableFromExcel.name +
                  '.json'
              }
            });
          }
        }, function() {
          JobLoggingService.error({
            message: 'variable-management.log-messages.variable' +
              '.unable-to-read-file',
            messageParams: {
              dataSet: dataSet.dataSetName,
              file: variableFromExcel.name +
                '.json'
            }
          });
        });
    };

    var createVariableResources = function(dataSet) {
      var variablesResources = [];
      return $q(function(resolve) {
        ExcelReaderService.readFileAsync(dataSet.excelFile)
          .then(function(variables) {
            if (!variables || variables.length === 0) {
              resolve(variablesResources);
              return;
            }
            //TODO here pipe the index to the build method
            var chainedJsonFileReader = $q.when();
            variables.forEach(function(variableFromExcel,
              variableIndex) {
              if (variableFromExcel.name) {
                if (dataSet.jsonFiles[variableFromExcel.name]) {
                  chainedJsonFileReader = chainedJsonFileReader.then(
                    function() {
                      return createJsonFileReader(dataSet,
                        variableFromExcel,
                        variablesResources, (variableIndex + 1));
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
              }
            });
            chainedJsonFileReader.finally(function() {
              resolve(variablesResources);
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

    var deleteAllVariablesNotProvidedByUser = function() {
      var promiseChain = $q.when();
      _.each(existingVariables, function(existingVariable) {
        if (!existingVariable.providedByUser) {
          promiseChain = promiseChain.then(function() {
            return VariableResource.delete({
                id: existingVariable.id
              }).$promise
              .catch(
                function(error) {
                  console.log('Error when deleting variable:',
                    error);
                });
          });
        }
      });
      return promiseChain;
    };

    var uploadVariable = function(variable, index,
      previouslyUploadedVariableNames) {
      if (previouslyUploadedVariableNames[variable.name]) {
        // duplicate variable name
        JobLoggingService.error({
          message: 'variable-management.log-messages.variable.duplicate-name',
          messageParams: {
            index: index + 1,
            name: variable.name,
            dataSetNumber: variable.dataSetNumber
          }
        });
        return $q.resolve();
      }
      return variable.$save().then(function() {
        previouslyUploadedVariableNames[variable.name] = true;
        JobLoggingService.success();
      }).catch(function(error) {
        var errorMessages = ErrorMessageResolverService
          .getErrorMessage(error, 'variable');
        JobLoggingService.error({
          message: errorMessages.message,
          messageParams: errorMessages.translationParams,
          subMessages: errorMessages.subMessages
        });
      });
    };

    var uploadDataSets = function(dataSetIndex) {
      var previouslyUploadedVariableNames = {};
      if (dataSetIndex === _.size(filesMap)) {
        deleteAllVariablesNotProvidedByUser().finally(function() {
          ElasticSearchAdminService.processUpdateQueue('variables').finally(
            function() {
              JobLoggingService.finish(
                'variable-management.log-messages.variable.upload-terminated', {
                  total: JobLoggingService.getCurrentJob().total,
                  warnings: JobLoggingService.getCurrentJob().warnings,
                  errors: JobLoggingService.getCurrentJob().errors
                });
              $rootScope.$broadcast('upload-completed');
            });
        });
        return;
      } else {
        var dataSet = _.filter(filesMap, function(filesObject) {
          return filesObject.dataSetIndex === dataSetIndex;
        })[0];
        if (!dataSet.excelFile) {
          JobLoggingService.error({
            message: 'variable-management.log-messages.variable.' +
              'missing-excel-file',
            messageParams: {
              dataSet: dataSet.dataSetName
            }
          });
          return uploadDataSets(dataSetIndex + 1);
        }
        createVariableResources(dataSet).then(function(variables) {
          var chainedVariableUploads = $q.when();
          if (variables) {
            variables.forEach(function(variable, index) {
              chainedVariableUploads = chainedVariableUploads.then(
                function() {
                  return uploadVariable(variable, index,
                    previouslyUploadedVariableNames);
                }
              );
            });
          }
          chainedVariableUploads.finally(function() {
            uploadDataSets(dataSetIndex + 1);
          });
        });
      }
    };

    var startJob = function(files, dataAcquisitionProjectId) {
      JobLoggingService.start('variable');
      createDataSetsFileMap(files, dataAcquisitionProjectId);
      uploadDataSets(0);
    };

    var uploadVariables = function(files, dataAcquisitionProjectId) {
      existingVariables = {};
      if (!CleanJSObjectService.isNullOrEmpty(
          dataAcquisitionProjectId)) {
        VariableRepositoryClient.findByDataAcquisitionProjectId(
          dataAcquisitionProjectId).then(function(result) {
          result.data.forEach(function(variable) {
            existingVariables[variable.id] = variable;
          });
          if (result.data.length > 0) {
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
              startJob(files, dataAcquisitionProjectId);
            });
          } else {
            startJob(files, dataAcquisitionProjectId);
          }
        });
      }
    };
    return {
      uploadVariables: uploadVariables
    };
  });
