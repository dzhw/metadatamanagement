/* global saveAs */
/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDetailController',
    function($scope, $translate, $stateParams, entity,
      DataAcquisitionProjectExportService, ExcelParser,
      Survey, SurveyCollection, SurveyDeleteResource,
      DataSet, DataSetDeleteResource, File, Upload,
      ZipReader, VariablesInputFilesReader, Variable,
      VariableDeleteResource) {
      $scope.dataAcquisitionProject = entity;
      $scope.objLists = {
        surveyList: {
          $resolved: false
        },
        variableList: {},
        dataSetList: {}
      };
      $scope.dataAcquisitionProject.id = $stateParams.id;
      $scope.elementsCounts = {
        surveys: 0,
        dataSets: 0,
        variables: 0
      };
      $scope.initUploadStatus = function(itemsToUpload) {
        $scope.uploadStatus = {
          itemsToUpload: itemsToUpload,
          errors: 0,
          successes: 0,
          logMessages: [{
            message: $translate.instant('metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.intro') +
              '\n'
          }],
          getProgress: function() {
            return this.errors + this.successes;
          },
          getResult: function() {
            if (this.errors > 0) {
              return 'danger';
            }
            return 'success';
          },
          getProgressState: function() {
            return this.getProgress() + '/' + this.itemsToUpload;
          },
          pushError: function(error) {
            //add an empty line
            $scope.uploadStatus.logMessages.push({
              message: '\n'
            });

            // if the error is already a string simply display it
            if (typeof error === 'string' || error instanceof String) {
              this.logMessages.push({
                message: error + '\n',
                type: 'error'
              });
            }

            // log the dataset id
            if (error.config && error.config.data && error.config.data.id) {
              this.logMessages.push({
                message: $translate.instant('metadatamanagementApp' +
                  '.dataAcquisitionProject.detail.' +
                  'logMessages.dataSetNotSaved', {
                    id: error.config.data.id
                  }) + '\n',
                type: 'error'
              });
            }

            //create additional information for the unsaved dataset
            if (error.data && error.data.errors) {
              error.data.errors.forEach(function(error) {
                $scope.uploadStatus.logMessages.push({
                  message: error.message + '\n',
                  type: 'error'
                });
              });
            } else if (error.data && error.data.status === 500) {
              $scope.uploadStatus.logMessages.push({
                message: $translate.instant('metadatamanagementApp' +
                  '.dataAcquisitionProject.detail.logMessages.' +
                  'internalServerError') + '\n',
                type: 'error'
              });
            } else if (error.data && error.data.message) {
              this.logMessages.push({
                message: error.data.message +
                  '\n',
                type: 'error'
              });
            }
            this.errors++;
          },
          pushSuccess: function() {
            this.successes++;
          }
        };
      };

      $scope.initUploadStatus(0);

      var saveSurveys = function(surveys) {
        $scope.initUploadStatus(surveys.length);
        SurveyDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id
          }, {},
          function() {
            for (var i = 0; i < surveys.length; i++) {
              var data = surveys[i];
              if (!data.id || data.id === '') {
                $scope.uploadStatus.pushError($translate.instant(
                  'metadatamanagementApp.dataAcquisitionProject.detail.' +
                  'logMessages.missingId', {
                    index: i + 1
                  }));
              } else {
                var surveyObj = {
                  id: data.id,
                  dataAcquisitionProjectId: $scope.dataAcquisitionProject
                    .id,
                  questionnaireId: data.questionnaireId,
                  title: {
                    en: data['title.en'],
                    de: data['title.de']
                  },
                  fieldPeriod: {
                    start: data['fieldPeriod.start'],
                    end: data['fieldPeriod.end']
                  }
                };
                var survey = new Survey(surveyObj);
                survey.$save().then(function() {
                  $scope.uploadStatus.pushSuccess();
                }).catch(function(error) {
                  $scope.uploadStatus.pushError(error);
                });
              }
            }
          },
          function(error) {
            $scope.uploadStatus.pushError(error);
          });
      };

      var saveDataSets = function(dataSets) {
        $scope.initUploadStatus(dataSets.length);
        DataSetDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id
          }, {},
          function() {
            for (var i = 0; i < dataSets.length; i++) {
              var data = dataSets[i];
              if (!data.id || data.id === '') {
                $scope.uploadStatus.pushError($translate.instant(
                  'metadatamanagementApp.dataAcquisitionProject.' +
                  'detail.logMessages.' +
                  'missingId', {
                    index: i + 1
                  }));
              } else {
                var dataSetObj = {
                  id: data.id,
                  dataAcquisitionProjectId: $scope.dataAcquisitionProject
                    .id,
                  questionnaireId: data.questionnaireId,
                  description: {
                    en: data['description.en'],
                    de: data['description.de']
                  },
                  variableIds: data.variableIds.replace(/ /g, '').split(
                    ','),
                  surveyIds: data.surveyIds.replace(/ /g, '').split(',')
                };
                var dataSet = new DataSet(dataSetObj);
                dataSet.$save().then(function() {
                  $scope.uploadStatus.pushSuccess();
                }).catch(function(error) {
                  $scope.uploadStatus.pushError(error);
                });
              }
            }
          });
      };
      var saveVariables = function(variables) {
        $scope.initUploadStatus(variables.length);
        VariableDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id
          }, {},
          function() {
            for (var i = 0; i < variables.length; i++) {
              var variable = variables[i];
              if (!variable.id || variable.id === '') {
                $scope.uploadStatus.pushError($translate.instant(
                  'metadatamanagementApp.dataAcquisitionProject.' +
                  'detail.logMessages.' +
                  'missingId', {
                    index: i + 1
                  }));
              } else {
                var variableObj = new Variable(variable);
                variableObj.$save().then(function() {
                  $scope.uploadStatus.pushSuccess();
                }).catch(function(error) {
                  $scope.uploadStatus.pushError(error);
                });
              }
            }
          });
      };
      $scope.onSurveyUpload = function(file) {
        if (file !== null) {
          ExcelParser.readFileAsync(file)
            .then(saveSurveys);
        }
      };
      $scope.onDataSetUpload = function(file) {
        if (file !== null) {
          ExcelParser.readFileAsync(file).then(saveDataSets);
        }
      };
      $scope.onVariablesUpload = function(file) {
        if (file !== null) {
          ZipReader.readZipFileAsync(file)
            .then(function(data) {
              saveVariables(VariablesInputFilesReader.readAllFiles(data,
                $scope.dataAcquisitionProject.id));
            });
        }
      };

      $scope.onTexTemplateUpload = function(file) {
        Upload.upload({
          url: 'api/files',
          fields: {
            'id': $scope.dataAcquisitionProject.id
          },
          file: file
        }).success(function(gridFsFileName) {
          console.log(gridFsFileName);
          File.download({
            fileName: gridFsFileName
          }, function(data) {
            console.log(data);
            saveAs(data.response, $scope.dataAcquisitionProject.id +
              '_Report.tex');
          });
        });

      };

      $scope.exportToODT = function() {
        DataAcquisitionProjectExportService.exportToODT($scope.rdcProject);
      };
      $scope.$watch('uploadStatus.getProgress()', function(newUploadStatus) {
        if (newUploadStatus === $scope.uploadStatus.itemsToUpload) {
          $scope.$broadcast('refresh');
        }
      });

    });
