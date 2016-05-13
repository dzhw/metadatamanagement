'use strict';

angular.module('metadatamanagementApp')
    .controller('DatapackageDetailController', function($scope,
      dataAcquisitionProject, surveyCollection, dataSetCollection) {
      $scope.dataAcquisitionProject = dataAcquisitionProject;
      console.log(dataAcquisitionProject);
      console.log(surveyCollection);
      console.log(dataSetCollection);
      $scope.datasetList = [
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            },
            {
              name: 'HURZ',
              variables: 'HURZ',
              cases: 'HURZ',
              format: 'HURZ',
              report: 'HURZ Hurz Hurz Hurz Hurz Hurz HURZHurz Hurz HurzHurzHurz'
            }
        ];
    });
