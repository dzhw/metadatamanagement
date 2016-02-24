/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDetailController',
    function($scope, $translate, entity, DataAcquisitionProjectExportService,
      ExcelParser, Survey) {
      $scope.dataAcquisitionProject = entity;
      $scope.initUploadStatus = function(itemsToUpload) {
        $scope.uploadStatus = {
          itemsToUpload: itemsToUpload,
          errors: 0,
          successes: 0,
          logMessages: [{
            message: $translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.intro') + '\n'
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
            if (typeof error === 'string') {
              this.logMessages.push({message: error + '\n', type: 'error'});
            }

            // log the dataset id
            if (error.config && error.config.data && error.config.data.id) {
              this.logMessages.push({
                message: $translate.instant('metadatamanagementApp' +
                '.dataAcquisitionProject.detail.logMessages.dataSetNotSaved',
                {id: error.config.data.id}) + '\n',
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
        for (var i = 0; i < surveys.length; i++) {
          var data = surveys[i];
          if (!data.id || data.id === '') {
            $scope.uploadStatus.pushError($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.logMessages.' +
            'missingId',{index: i + 1}));
          } else {
            var surveyObj = {
              id: data.id,
              dataAcquisitionProjectId: $scope.dataAcquisitionProject.id,
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
      };
      $scope.onSurveyUpload = function(file) {
        if (file !== null) {
          ExcelParser.readFileAsync(file)
            .then(saveSurveys);
        }
      };
      $scope.exportToODT = function() {
        DataAcquisitionProjectExportService.exportToODT($scope.rdcProject);
      };
    });
