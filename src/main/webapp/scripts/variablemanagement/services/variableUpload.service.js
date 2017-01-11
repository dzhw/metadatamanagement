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

    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'variable-management.log-messages.variable.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          console.log(objects);
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (objects[uploadCount] === null) {
          uploadCount++;
          return upload();
        } else if (!objects[uploadCount].name || objects[uploadCount]
          .name === '') {
          var index = uploadCount;
          JobLoggingService.error({
            message: 'variable-management.log-messages.variable.missing-id',
            messageParams: {index: index + 1}
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
          uploadCount = 0;
          objects = [];
          JobLoggingService.start('variable');
          VariableDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: dataAcquisitionProjectId}).$promise.then(
            function() {
              var index = 0;
              var filesMap = {};
              var promisesPool = [];
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
                    filesMap[path[pathLength - 2]].dataSet =
                    path[pathLength - 2];
                    filesMap[path[pathLength - 2]].index = index;
                    filesMap[path[pathLength - 2]].jsonFiles = {};
                    index++;
                  }
                  filesMap[path[pathLength - 2]].excelFile = file;
                }
                if (file.name.endsWith('.json')) {
                  var variableName = _.split(file.name, '.json')[0];
                  if (!filesMap[path[pathLength - 3]]) {
                    filesMap[path[pathLength - 3]] = {};
                    filesMap[path[pathLength - 3]].dataSet =
                    path[pathLength - 3];
                    filesMap[path[pathLength - 3]].index = index;
                    filesMap[path[pathLength - 3]].jsonFiles = {};
                    index++;
                  }
                  filesMap[path[pathLength - 3]].jsonFiles[variableName] = file;
                }
              });
              index = 0;
              var getNextDataSetObject = function() {
                if (index === (_.size(filesMap))) {
                  upload();
                } else {
                  var files = _.filter(filesMap, function(o) {
                    return o.index === index ;
                  })[0];
                  console.log(files);
                  if (!files.excelFile) {
                    JobLoggingService.error({
                      message:
                      'variable-management.' +
                      'log-messages.variable.missing-excel-file',
                      messageParams: {
                        dataSet: files.dataSet
                      }
                    });
                    index++;
                    return getNextDataSetObject();
                  }
                  ExcelReaderService.readFileAsync(files.excelFile)
                  .then(function(variables) {
                                variables.forEach(function(variableFromExcel) {
                                  if (files.jsonFiles[variableFromExcel.name]) {
                                    promisesPool.push(FileReaderService
                                      .readAsText(files
                                        .jsonFiles[variableFromExcel.name])
                                        .then(function(variableAsText) {
                                          try {
                                            var variableFromJson = JSON
                                            .parse(variableAsText);
                                            objects.push(VariableBuilderService
                                              .buildVariable(variableFromExcel,
                                                variableFromJson,
                                                dataAcquisitionProjectId,
                                                files.dataSet));
                                          } catch (e) {
                                            JobLoggingService.error({
                                              message: 'variable-management.' +
                                      'log-messages.variable.json-parse-error',
                                              messageParams: {
                                                dataSet: files.dataSet,
                                                file: variableFromExcel.name +
                                                '.json'}
                                            });

                                          }}, function() {
                                      JobLoggingService.error({
                                        message: 'variable-management.' +
                                    'log-messages.variable.unable-to-read-file',
                                        messageParams: {
                                          dataSet: files.dataSet,
                                          file: variableFromExcel.name + '.json'
                                        }
                                      });
                                    }));
                                  } else {
                                    JobLoggingService.error({
                                      message:
                                      'variable-management.' +
                                      'log-messages.variable.missing-json-file',
                                      messageParams: {
                                        dataSet: files.dataSet,
                                        name: variableFromExcel.name + '.json'
                                      }
                                    });
                                  }
                                });
                              }, function() {
                                console.log('kf');
                                JobLoggingService.error({
                                  message: 'variable-management.' +
                                  'log-messages.variable.unable-to-read-file',
                                  messageParams: {
                                    dataSet: files.dataSet,
                                    file: 'variables.xlsx'
                                  }
                                });
                              }).then(function() {
                      return $q.all(promisesPool);
                    }).then(function() {
                      index++;
                      getNextDataSetObject();
                    });
                }
              };
              getNextDataSetObject();
            }, function() {
              JobLoggingService.cancel(
                'variable-management.log-messages.variable.unable-to-delete');
              return $q.reject();
            }
          );
        });
      }
    };

    return {
      uploadVariables: uploadVariables
    };
  });
