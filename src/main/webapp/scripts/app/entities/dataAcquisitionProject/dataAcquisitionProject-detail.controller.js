/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp')
    .controller('DataAcquisitionProjectDetailController',
      function($scope, entity, DataAcquisitionProjectExportService,
        ExcelParser, Survey) {
        $scope.dataAcquisitionProject = entity;
        $scope.show = false;
        $scope.progress = 0;
        $scope.isOpen = false;
        $scope.errorMessages = [];
        var saveSurveys = function(surveys) {
          var length = surveys.length;
          $scope.maxProgress = length;
          $scope.uploadResult = 'success';
          for (var i = 0; i < length; i++) {
            var data = surveys[i];
            console.log(data.questionnaireId);
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
              $scope.progress = $scope.progress + 1;
              $scope.state = $scope.progress + '/' + $scope.maxProgress;
            }).catch(function(errors) {
              $scope.show = true;
              $scope.isOpen = true;
              $scope.progress = $scope.progress + 1;
              $scope.uploadResult = 'danger';
              $scope.errorMessages.push(errors);
              $scope.state = $scope.progress + '/' + $scope.maxProgress;

            });
          }
        };
        $scope.onSurveyUpload = function(file) {
          if (file !== null) {
            $scope.show = false;
            $scope.isOpen = false;
            $scope.progress = 0;
            $scope.uploadResult = 'success';
            $scope.errorMessages = [];
            ExcelParser.readFileAsync(file)
            .then(function(fileInputContent) {
              saveSurveys(fileInputContent);
            });
          }
        };
        $scope.exportToODT = function() {
          DataAcquisitionProjectExportService.exportToODT($scope.rdcProject);
        };
      });
