/* global saveAs */
/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDetailController',
    function($scope, $translate, $stateParams, entity,
      DataAcquisitionProjectExportService, ExcelParser,
      Survey, SurveyCollection, SurveyDeleteResource,
      DataSet, DataSetDeleteResource, FileResource, Upload,
      ZipReader, VariablesInputFilesReader, Variable,
      VariableDeleteResource, CustomModal, AtomicQuestion,
      AtomicQuestionDeleteResource) {
      $scope.dataAcquisitionProject = entity;
      $scope.objLists = {
        surveyList: {},
        variableList: {},
        dataSetList: {},
        atomicQuestionsList: {}
      };
      $scope.dataAcquisitionProject.id = $stateParams.id;

      $scope.initUploadStatus = function(itemsToUpload, buttonsState, element) {
        $scope.uploadStatus = {
          itemsToUpload: itemsToUpload,
          uploadedElement: element,
          disableButton: buttonsState,
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

      $scope.initUploadStatus(0, false, null);

      var saveSurveys = function(surveys) {
        $scope.initUploadStatus(surveys.length, true, 'surveys-uploaded');
        SurveyDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id
          },
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
        $scope.initUploadStatus(dataSets.length, true, 'datasets-uploaded');
        DataSetDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id
          },
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
          },
          function(error) {
            $scope.uploadStatus.pushError(error);
          });
      };
      var saveAtomicQuestions = function(atomicQuestions) {
        $scope.initUploadStatus(atomicQuestions.length, true,
          'atomicQuestions-uploaded');
        AtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id
          },
          function() {
            for (var i = 0; i < atomicQuestions.length; i++) {
              var data = atomicQuestions[i];
              if (!data.id || data.id === '') {
                $scope.uploadStatus.pushError($translate.instant(
                  'metadatamanagementApp.dataAcquisitionProject.' +
                  'detail.logMessages.' +
                  'missingId', {
                    index: i + 1
                  }));
              } else {
                var atomicQuestionObj = {
                  id: data.id,
                  name: data.name,
                  dataAcquisitionProjectId: $scope.dataAcquisitionProject
                    .id,
                  questionnaireId: data.questionnaireId,
                  variableId: data.variableId,
                  footnote: {
                    en: data['footnote.en'],
                    de: data['footnote.de']
                  },
                  compositeQuestionName: data.compositeQuestionName,
                  instruction: {
                    en: data['instruction.en'],
                    de: data['instruction.de']
                  },
                  introduction: {
                    en: data['introduction.en'],
                    de: data['introduction.de']
                  },
                  questionText: {
                    en: data['questionText.en'],
                    de: data['questionText.de']
                  },
                  sectionHeader: {
                    en: data['sectionHeader.en'],
                    de: data['sectionHeader.de']
                  },
                  type: {
                    en: data['type.en'],
                    de: data['type.de']
                  }
                };

                var atomicQuestion = new AtomicQuestion(atomicQuestionObj);
                atomicQuestion.$save().then(function() {
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
      var saveVariables = function(variables) {
        $scope.initUploadStatus(variables.length, true, 'variables-uploaded');
        VariableDeleteResource.deleteByDataAcquisitionProjectId({
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id
          },
          function() {
            for (var i = 0; i < variables.length; i++) {
              var variable = variables[i];
              if (!variable.id || variable.id === '') {
                $scope.uploadStatus.pushError($translate.instant(
                  'metadatamanagementApp.dataAcquisitionProject.' +
                  'detail.logMessages' +
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
          },
          function(error) {
            $scope.uploadStatus.pushError(error);
          });
      };
      $scope.onSurveyUpload = function(file) {
        if (file !== null) {
          CustomModal.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteSurveys', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              ExcelParser.readFileAsync(file)
                .then(saveSurveys);
            }
          });
        }
      };
      $scope.onDataSetUpload = function(file) {
        if (file !== null) {
          CustomModal.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteDataSets', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              ExcelParser.readFileAsync(file).then(saveDataSets);
            }
          });
        }
      };
      $scope.onVariablesUpload = function(file) {
        if (file !== null) {
          CustomModal.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteVariables', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              ZipReader.readZipFileAsync(file)
                .then(function(data) {
                  saveVariables(VariablesInputFilesReader.readAllFiles(
                    data,
                    $scope.dataAcquisitionProject.id));
                });
            }
          });
        }
      };
      $scope.onAtomicQuestionUpload = function(file) {
        if (file !== null) {
          CustomModal.getModal($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'deleteMessages.deleteAtomicQuestions', {
              id: $scope.dataAcquisitionProject.id
            })).then(function(returnValue) {
            if (returnValue) {
              ExcelParser.readFileAsync(file).then(saveAtomicQuestions);
            }
          });
        }
      };

      //Generate Variable Report
      $scope.onTexTemplateUpload = function(file) {
        //Upload Tex-File with freemarker commands
        $scope.initUploadStatus(1, true, 'generate-variable-report');
        Upload.upload({
          url: 'api/data-sets/report',
          fields: {
            'id': $scope.dataAcquisitionProject.id
          },
          file: file
            //Upload and document could filled with data successfully
        }).success(function(gridFsFileName) {
          //Download automaticly data filled tex template
          FileResource.download({
            fileName: gridFsFileName
          }, function(data) {
            saveAs(data.response, $scope.dataAcquisitionProject.id +
              '_Report.tex');
          });
          $scope.uploadStatus.pushSuccess();
          //Server hat issues with the tex file, send error to error output
        }).error(function(error) {
          var endErrorIndex = error.message.indexOf('----');
          var messageShort = error.message.substr(0, endErrorIndex).trim();
          $scope.uploadStatus.pushError(messageShort);
        });
      };

      $scope.exportToODT = function() {
        DataAcquisitionProjectExportService.exportToODT($scope.rdcProject);
      };
      $scope.$watch('uploadStatus.getProgress()', function(newUploadStatus) {
        if (newUploadStatus === $scope.uploadStatus.itemsToUpload) {
          $scope.uploadStatus.disableButton = false;
          $scope.$broadcast($scope.uploadStatus.uploadedElement);
        }
      });
    });
