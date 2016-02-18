/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectDetailController',
    function($scope, entity, DataAcquisitionProjectExportService,
      ExcelParser, Survey) {
      $scope.dataAcquisitionProject = entity;
      $scope.fileInputContent = '';
      $scope.files = [];
      $scope.messages = [];
      var putData = function(surveys) {
        for (var i = 0; i < surveys.length; i++) {
          var data = surveys[i];
          var surveyObj = {
            id: data.rdcId,
            dataAcquisitionProjectId: $scope.dataAcquisitionProject.id,
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
          survey.$save().then(function(success, error) {
            if (error) {
              $scope.messages.push({
                status: 'error',
                data: error
              });
            } else {
              $scope.messages.push({
                status: 'success',
                data: success
              });
            }
          });
        }
      };
      $scope.$watch('files', function() {
        if ($scope.files.length > 0) {
          ExcelParser.readFileAsync($scope.files[0])
            .then(function(fileInputContent) {
              //$scope.fileInputContent = fileInputContent;
              putData(fileInputContent);
            });
        }
      });

      /*$scope.$watch('fileInputContent',function() {
        putData($scope.fileInputContent);
      });*/

      $scope.exportToODT = function() {
        DataAcquisitionProjectExportService
          .exportToODT($scope.dataAcquisitionProject);
      };
    });
