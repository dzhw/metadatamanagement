'use strict';

angular.module('metadatamanagementApp')
  .controller('ChoosePreviousStudyVersionController',
    function(StudyVersionsResource, studyId, $scope) {
      StudyVersionsResource.get({id: studyId}).$promise.then(
        function(studies) {
          console.log(studies);
          $scope.studies = studies;
        });
    });
