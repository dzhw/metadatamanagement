'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function($scope, entity, SurveyReferencedResource,
      DataSetReferencedResource) {
      $scope.study = entity;
      $scope.cleanedAccessWays = '';

      $scope.$watch('study', function() {
        if ($scope.study.$resolved) {
          $scope.cleanedAccessWays = '' + $scope.study.accessWays + '"';
          $scope.cleanedAccessWays = $scope.cleanedAccessWays
          .replace(/[\[\]'"]/g, '');
          SurveyReferencedResource.findByDataAcquisitionProjectId(
            {id: $scope.study.id},
            function(surveys) {
              $scope.surveys = surveys._embedded.surveys;
            });
          DataSetReferencedResource.findByDataAcquisitionProjectId(
            {id: $scope.study.id},
            function(dataSets) {
              $scope.dataSets = dataSets._embedded.dataSets;
              console.log($scope.dataSets);
            });
          console.log($scope.study);
        } else {
          console.log(false);
        }
      }, true);
    });
