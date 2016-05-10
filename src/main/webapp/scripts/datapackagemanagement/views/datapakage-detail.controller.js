'use strict';

angular.module('metadatamanagementApp')
    .controller('DatapackageDetailController', function($scope,
      dataAcquisitionProject, surveyCollection, dataSetCollection) {
      $scope.dataAcquisitionProject = dataAcquisitionProject;
      console.log(dataAcquisitionProject);
      console.log(surveyCollection);
      console.log(dataSetCollection);
    });
