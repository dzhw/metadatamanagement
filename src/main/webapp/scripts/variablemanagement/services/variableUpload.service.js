/*jshint loopfunc: true */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableUploadService',
  function(VariableBuilderService, VariableRepositoryClient,
    JobLoggingService,
    ErrorMessageResolverService, $q, FileReaderService,
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
        if (file.name.endsWith('.json')) {
          var variableName = _.split(file.name, '.json')[0];
          var dataSetName = path[pathLength - 2];
          if (!filesMap[dataSetName]) {
            filesMap[dataSetName] = {};
            filesMap[dataSetName].dataAcquisitionProjectId =
              dataAcquisitionProjectId;
            filesMap[dataSetName].dataSetName = dataSetName;
            filesMap[dataSetName].dataSetNumber =
              _.split(dataSetName, 'ds')[1];
            filesMap[dataSetName].dataSetIndex = dataSetIndex;
            filesMap[dataSetName].jsonFiles = {};
            dataSetIndex++;
          }
          filesMap[dataSetName].jsonFiles[variableName] = file;
        }
      });
    };

    var createVariableResourceFromFile = function(variableName, dataSet,
      jsonFile) {
      return FileReaderService.readAsText(jsonFile)
        .then(function(variableAsText) {
          try {
            var variableFromJson = JSON.parse(
              variableAsText);
            variableFromJson.name = variableName;

            var variableResource = VariableBuilderService
              .buildVariable(variableFromJson, dataSet);
            if (existingVariables[variableResource.id]) {
              existingVariables[variableResource.id]
                .providedByUser = true;
            }
            return variableResource;
          } catch (e) {
            console.log(e);
            JobLoggingService.error({
              message: 'variable-management.log-messages.' +
                'variable.json-parse-error',
              messageParams: {
                dataSet: dataSet.dataSetName,
                file: variableName +
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
              file: variableName +
                '.json'
            }
          });
        });
    };

    var uploadVariable = function(variable) {
      return variable.$save().then(function() {
        JobLoggingService.success();
      }).catch(function(error) {
        var errorMessages = ErrorMessageResolverService
          .getErrorMessage(error, 'variable');

        if (errorMessages.subMessages.length > 0) {
          for (var i = 0; i < errorMessages.subMessages.length; ++i) {
            errorMessages.subMessages[i].translationParams.variableName =
              variable.name;
          }
        }

        JobLoggingService.error({
          message: errorMessages.message,
          messageParams: errorMessages.translationParams,
          subMessages: errorMessages.subMessages
        });
      });
    };

    var createVariableUploadChain = function(dataSet) {
      var uploadChain = $q.when();
      _.forEach(dataSet.jsonFiles, function(jsonFile, variableName) {
          uploadChain = uploadChain.then(
            function() {
              return createVariableResourceFromFile(variableName, dataSet,
                jsonFile);
            }).then(uploadVariable);
        });
      return uploadChain;
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

    var uploadDataSets = function(dataSetIndex) {
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
        createVariableUploadChain(dataSet).finally(function() {
            uploadDataSets(dataSetIndex + 1);
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
