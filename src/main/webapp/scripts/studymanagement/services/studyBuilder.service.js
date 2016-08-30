/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService) {
    var getStudies = function(studies, projectId) {
      var studiesObjArray = [];
      for (var i = 0; i < studies.length; i++) {
        var data = studies[i];
        var studyObj = {
          id: data.id,
          dataAcquisitionProjectId: projectId
        };
        var cleanedStudyObject = CleanJSObjectService
          .removeEmptyJsonObjects(studyObj);
        studiesObjArray[i] = new StudyResource(cleanedStudyObject);
      }
      return studiesObjArray;
    };
    return {
      getStudies: getStudies
    };
  });
